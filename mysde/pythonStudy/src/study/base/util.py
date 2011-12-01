'''
Created on Sep 25, 2011

@author: ehaijsu
'''

class FileUtil:
    '''
    classdocs
    '''
    def __init__(self, fileName):
        '''
        Constructor
        '''
        self.fileName = fileName

    '''
    intf.FileHandler
    '''
    def readlines(self, fileHandler):
        openfile = open(self.fileName, 'rU')
        try:
            for line in openfile:
                line = line.rstrip("\n")
                fileHandler.processLine(line)
        finally:
            openfile.close()




