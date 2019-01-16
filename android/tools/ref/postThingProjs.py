#!/usr/bin/python3
# 依赖文件：         table-projs-clear.txt
# 项目与作者映射表，详见： getAuthorOfProj

sHelpModule = \
'''该模块命令需要提供参数运行:
  postThingProjs c p:b z r:010,020 t:3 j:020-cont-add-remove
  c 表示注入注释，把项目根目录下的 README.md 内容注入到 src/Main.ts 文件的头部
  p 表示发布项目 其选项b表示在publish前强制build
  z 表示打包项目为zip
  r 表示只处理指定数字ID对应的所有项目，多个数字ID用英文逗号连接 由于含义冲突，如指定j则r会被忽略!
  t 表示测试 其选项为项目个数
  j 表示只处理指定编号的项目，多个项目用英文逗号连接
  h 打印本命令，使用该参数会导致忽略其他任何参数！
(细节：参数如需要选项，均通过冒号方式连接，冒号后边紧跟选项，中间不得有空格！)
'''

import codecs
import os
import re
import traceback
import sys

# 从 README.md 读取信息
def getComment( pProjBase, sAuthor, sCopyRight, lenCommentLine ):
    pReadmeFile = os.path.join( pProjBase, "README.md" );
    if not os.path.isfile( pReadmeFile ):
        print ("no hit:", pReadmeFile );
        return;

    fsReadme = open( pReadmeFile, "r", encoding="utf8", newline='\n'  );
    try:
        sReadme = fsReadme.read();
    except:
        traceback.print_exc();
        print( "error read:", pReadmeFile );
    else:
        # 清除HTML标签并替换为换行
        sReadme = re.sub( "<[^>]+>", "\n", sReadme );
        # 统一换行格式
        sReadme = sReadme.replace( "\r", "\n" ).replace( " ", "" ).strip( ' \t\n\r' );
        # 合并多余换行
        sReadme = re.sub( "\n+", "\n", sReadme );
        # 分割过长的单行
        sReadme = ("\n").join( [ ("\n").join([ sLine [i:i+lenCommentLine] for i in range(0, len(sLine), lenCommentLine) ]) for sLine in sReadme.split( "\n" ) ] );
        # 将每行作为一个段落进行截取
        sReadme = re.sub( "[\n\r]+", "\n" + " *      ", sReadme );
        return \
            '/**\n * @copyright '+ sCopyRight\
             + '\n * @author ' + sAuthor\
             + '\n * @desc ''' + sReadme\
             + '\n */';
    finally:
        fsReadme.close()

# 向项目的Main.ts 注入 README.md 中的介绍信息
def injectComment( pProjBase, sAuthor, sCopyRight, lenCommentLine ):

    pTSFile = os.path.join( pProjBase, "src/Main.ts" );
    if not os.path.isfile( pTSFile ):
        print ("\tno hit:", pTSFile );
        return;

    # 判断并删除 BOM
    fsTSBIN = open( pTSFile, 'rb' )
    binTS = fsTSBIN.read(3)
    if binTS == '\xEF\xBB\xBF':
        fsTSBIN.seek( 0 )
        binTS = fsTSBIN.read()
        fsTSBIN.close()

        fsTSBIN = open( name,'wb' )
        fsTSBIN.write( binTS )
        fsTSBIN.close()
        print( '\tbom in', pTSFile, 'croped!' )

    # 文本方式读取TS文件 
    fsTS = open( pTSFile, "r", encoding="utf8", newline='' );
    sCont = fsTS.read();
    fsTS.close(); 

    # 删除TS文件中的头部区段注释
    sCont = re.sub( "\s*\/\*.*\*\/\s*(class Main)", "class Main", sCont, 0, re.DOTALL );

    # 打开文件重新写入
    cmt = getComment( pProjBase, sAuthor, sCopyRight, lenCommentLine );
    if cmt:
        sContFill = cmt + '\n\n' + sCont;
        fsTS = open( pTSFile, "w", encoding="utf8", newline='' );
        fsTS.write( sContFill );
        fsTS.flush();
        fsTS.close();
        print( "\tcomment inject OK!:", pTSFile );
    else:
        print( "\tcomment less!!!!" )

# 获取项目的作者
def getAuthorOfProj( sProjID ):

    sCHID = sProjID[0:3];
    if sCHID in [ "010", "040", "050", "070", "180" ]:
        return "city";
    elif sCHID in [ "020", "080", "175"]:
        return "dily";
    elif sCHID in [ "060", "140", "160" ]:
        return "A闪"; #""
    elif sCHID in [ "030", "100", "110", "120", "130", "150" ]:
        return "yjtx";
    elif sCHID in [ "090", "170" ]:
        return "东北大客"; #""

# pub proj
def pubProj( pProjSetBase, sProjID, bForceBuild ):
    
    # 确保必需的 Egret 项目文件，
    if not os.path.isfile( pProjSetBase +'/'+ sProjID +'/egretProperties.json' ):
        print( "\tNot a legal Egret proj!!!!:", sProjID );
        return;
    
    # 没有build先要build
    if bForceBuild or not os.path.isfile( pProjSetBase +'/'+ sProjID +'/libs/modules/egret/egret.min.js'):
        sCmd = 'egret b '+ pProjSetBase +'/'+ sProjID +'/ > nul';
        print( "\t--> build:", sCmd );
        fsPubProj = os.system( sCmd );

    sCmd = 'egret publish '+ pProjSetBase +'/'+ sProjID +'/ --version 001 > nul';
    print( "\t--> publish:", sCmd );
    fsPubProj = os.system( sCmd );

    pIndexFile = pProjSetBase + '/' + sProjID + '/bin-release/web/001/index.html';
    if os.path.isfile( pIndexFile ):
        print( "\tpublish OK!:", sProjID );
    else:
        print( "\tpublish Fail!!!!:", sProjID );

# zip proj
def zipProj( pProjSetBase, sProjID ):
    sCmd = '7z a -tzip '+ pProjSetBase +'/'+ sProjID +'/'+ sProjID +'.zip'\
        +' '+ pProjSetBase +'/'+ sProjID + '/*.* -r -stl -up0q0'\
        + ' -xr@batch-zip-projs-list-excluded.lst >> batch-zip-projs.log';
    # 7z a -tzip ../projs/010-disp-basic/010-disp-basic.zip
    #  ../projs/010-disp-basic/*.* -r -stl -up0q0
    #  -xr@batch-zip-projs-list-excluded.lst >> batch-zip-projs.log
    fsZipProj = os.system( sCmd );

    pZipFile = pProjSetBase +'/'+ sProjID +'/'+ sProjID +'.zip';
    if os.path.isfile( pZipFile ):
        print( "\tzip OK!:", pZipFile );
    else:
        print( "\tzip Fail!!!!!", pZipFile );

def postThingProjs( bCmnt, bPub, bForceBuild,bZip, vcCHIDRange, vcProjExact, nLimitProj, lenCommentLine ):
    pTableFile = "table-projs-clear.txt";
    pProjSetBase = "../projs";

    # 过滤器裁定
    bFilterProj = bool( len( vcProjExact ) );

    # 读取项目列表
    fsTable = open( pTableFile, "r", encoding="utf8" );
    aTable = [ str.strip( ' \t\n\r' ) for str in fsTable.readlines() ];
    fsTable.close();

    # print( aTable );

    nThruProj = 0;
    nPrcessProj = 0;
    # 要处理的
    for sProjID in aTable:
        sAuthor = getAuthorOfProj( sProjID );
        if sAuthor and len( sAuthor ):
            if bFilterProj:
                if len(vcProjExact) and sProjID in vcProjExact or not len(vcProjExact):
                    pProjBase = pProjSetBase +"/"+ sProjID ;
                    if os.path.isdir( pProjBase ):  # 确认该项目ID对应的目录存在！
                        print( "process:", sProjID );
                        if bCmnt: injectComment( pProjBase, sAuthor, "www.egret.com", lenCommentLine )
                        if bPub: pubProj( pProjSetBase, sProjID, bForceBuild )
                        if bZip: zipProj( pProjSetBase, sProjID )
                        nPrcessProj +=1
                    else:
                        print( sProjID, "not a dir!!!!!" );
                else:
                    if not len(vcProjExact) : #没有指定范围时才输出
                        print( sProjID, "not in range from j: argv !!!!!", nLimitProj, nThruProj );

            else:
                if len(vcCHIDRange) and sProjID[0:3] in vcCHIDRange or not len(vcCHIDRange):
                    pProjBase = pProjSetBase +"/"+ sProjID ;
                    if os.path.isdir( pProjBase ):  # 确认该项目ID对应的目录存在！
                        print( "process:", sProjID );
                        if bCmnt: injectComment( pProjBase, sAuthor, "www.egret.com", lenCommentLine )
                        if bPub: pubProj( pProjSetBase, sProjID, bForceBuild )
                        if bZip: zipProj( pProjSetBase, sProjID )
                        nPrcessProj +=1
                    else:
                        print( sProjID, "not a dir!!!!!" );
                else:
                    if not len(vcCHIDRange) : #没有指定范围时才输出
                        print( sProjID, "not in range from r: argv !!!!!", nLimitProj, nThruProj );

            if nLimitProj > 0:  # 处理项目个数限制
                nThruProj += 1
                if nThruProj >= nLimitProj:break;

        else:
            print( sProjID, "not a valid proj!!!!!" );
    os.system( "echo success process "+str(nPrcessProj)+" proj!" );
    os.system( "@pause" );

# real start here >>>

if len(sys.argv) == 1 :
    print( "请提供足够的参数来运行命令，使用参数 h 获得使用说明" );
else:
    # argv parse
    lenCommentLine = 36;    #自动截行
    bCmnt = False;  # 是否处理注释
    bPub = False;   # 是否发布项目
    bForceBuild = False;   # 是否强制在发布项目前Build
    bZip = False;   # 是否打包项目
    bHelp = False;   # 是否打印帮助
    vcCHIDRange = [];   # 指定三位数字ID范围内的项目
    vcProjExact = [];   # 指定项目ID
    nLimitProj = 0; # >0 才生效
    for i in range(len(sys.argv)):
        param = sys.argv[i]
        # print( "arg", i, "is:", param )
        if "c" == param:
            bCmnt = True;
        elif "p" == param[0:1]:
            bPub = True;
            if 'b' in param[1:]: bForceBuild = True;
        elif "z" == param:
            bZip = True;
        elif "t:" == param[0:2]:
            nLimitProj = int(param[2:]);
            if type(nLimitProj) != int: nLimitProj = 0;
        elif "r:" == param[0:2]:
            vcCHIDRange = param[2:].split(",")
        elif "j:" == param[0:2]:
            vcProjExact = param[2:].split(",")
        elif "h" == param:
            bHelp = True;
            break;
    
    if bHelp:
        print( sHelpModule );
    else:
        # 设定工作目录
        pModuleIn = os.path.split(os.path.realpath(__file__))[0];
        os.chdir( pModuleIn );
        print( "working at:", os.getcwd() );
    
        postThingProjs( bCmnt, bPub, bForceBuild, bZip, vcCHIDRange, vcProjExact, nLimitProj, lenCommentLine );
