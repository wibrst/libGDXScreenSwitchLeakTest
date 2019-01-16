import glob
import os
import subprocess

sCvdMark = '[cvd]'

os.chdir( "../profiler-dump/" )
print( "工作目录： "+os.getcwd() )
for filename in glob.glob( r'*.hprof' ):
    if os.path.exists( sCvdMark + filename ):
        subprocess.call( ['move', filename, 'history'] )
        pass
    if filename.find( sCvdMark ) == -1:
        result = subprocess.call( ['hprof-conv.exe', filename, sCvdMark + filename] )
        print( filename, result )
        if result == 0:
            os.system( 'move ' + filename + ' history/' )
        pass
os.system("pause")
