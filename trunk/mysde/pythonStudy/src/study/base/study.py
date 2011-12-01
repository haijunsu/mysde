#!/usr/bin/env python
'''
Created on Sep 25, 2011
@author: ehaijsu
'''
from intf import FileHandler
from util import FileUtil

class Study(FileHandler):
    def __init__(self, fileName):
        FileHandler.__init__(self)
        self.fileName = fileName

    def processLine(self, line):
        print ">>>> " + line

if __name__ == '__main__':
    print "hello world!"
    t = Study("/Data/CNM5_PB4/application-management-easa_default.log")
    futil = FileUtil(t.fileName)
    futil.readlines(t)