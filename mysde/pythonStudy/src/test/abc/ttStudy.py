'''
Created on Sep 28, 2011

@author: ehaijsu
'''
from study.base.util import TextFile
import tarfile
import gzip

class TTStudy(TextFile):
    def __init__(self, fileName):
        TextFile.__init__(self, fileName)

    def processLine(self, line):
        print ">>>> " + line
        return True

if __name__ == '__main__':
    print "TTStudy: hello world!"
    t = TTStudy("/Data/CNM5_PB4/application-management-easa_default.log")
    t.readlines(t)
    print "======================================"
    lines = t.readlines()
    for line in lines:
        print "<<<< " + line
    print "======================================"

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
    for line in f.readlines():
        line = line.rstrip("\n")
        print line
        i = i + 1
        if (i > 10):
            print "exit"
            break
    f.close()
    tf = TextFile("aaa.txt")
    tf.write("content1")
    tf.write("content2")
    tf.write("content3")
    tf.write("content4")
    tf.flushAndOverride()
    print tf.lineCount()
    tf = TextFile("bbb.txt")
    tf.write("content1")
    tf.write("content2")
    tf.write("content3")
    tf.write("content4")
    tf.flushAndAppend()
    print tf.lineCount()
