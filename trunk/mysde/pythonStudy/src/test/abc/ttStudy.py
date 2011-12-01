'''
Created on Sep 28, 2011

@author: ehaijsu
'''
from study.base.util import FileUtil
import tarfile
import gzip

class TTStudy(FileUtil):
    def __init__(self, fileName):
        FileUtil.__init__(self, fileName)

    def processLine(self, line):
        print ">>>> " + line

if __name__ == '__main__':
    print "TTStudy: hello world!"
    t = TTStudy("/Data/CNM5_PB4/application-management-easa_default.log")
    t.readlines(t)

    tar = tarfile.open("C:\Data\CNM5_PB4\dm_easa_07062011.tar")
    for tarinfo in tar:
        print tarinfo.name, "is", tarinfo.size, "bytes in size and is",
        if tarinfo.isreg():
            print "a regular file."
        elif tarinfo.isdir():
            print "a directory."
        else:
            print "something else."
    tar.close()
    f = gzip.open('C:/Data/CNM5_PB4/vmstat_process_07062011/opt/nortel/data/eng/vmstat.201007.gz', 'rb')
    i = 0
    for line in f:
        line = line.rstrip("\n")
        print line
        i = i + 1
        if (i > 10):
            print "exit"
            break
    f.close()