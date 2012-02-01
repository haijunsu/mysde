'''
Created on Sep 25, 2011

@author: ehaijsu
'''
import gzip

class TextFile:
    '''
    classdocs
    '''
    def __init__(self, fileName):
        '''
        Constructor
        '''
        self.fileName = fileName
        self.memLines = []
    '''
    Open gzip text file or normal test file.
    gzip text file only support 'rb' mode and can't be modified.
    '''
    def openFile(self, mode):
        if (self.isGzipTextFile()):
            return gzip.open(self.fileName, 'rb')
        return open(self.fileName, mode)

    '''
    Judge the gzip text file according the file extension. If file extension is ".gz", return ture
    '''
    def isGzipTextFile(self):
        return self.fileName.lower().endswith(".gz")

    '''
    intf.FileHandler,
    Note: only support 1 handler
    '''
    def readlines(self, *fileHandler):
        openfile = self.openFile('rU')
        try:
            if(len(fileHandler) == 0):
                lines = []
                for line in openfile.readlines():
                    line = line.rstrip("\n")
                    lines.append(line.rstrip("\n"))
                return lines
            else:
                for line in openfile.readlines():
                    line = line.rstrip("\n")
                    if (fileHandler[0].processLine(line) == False) :
                        break
                return
        finally:
            openfile.close()


    def readFirstLine(self):
        openfile = self.openFile('rU')
        try:
            line = openfile.readline()
            return line.rstrip("\n")
        finally:
            openfile.close()

    def readLastLine(self):
        openfile = self.openFile('rU')
        try:
            line = "" #default is empty string
            lines = openfile.readlines()
            if (len(lines) > 0):
                line = lines[len(lines) - 1]
            return line.rstrip("\n")
        finally:
            openfile.close()

    def topLines(self, lineNum):
        openfile = self.openFile('rU')
        try:
            lines = openfile.readlines()
            lineCount = len(lines)
            if (lineCount > lineNum):
                lines = lines[0:lineNum]
            topLines = []
            for line in lines :
                topLines.append(line.rstrip("\n"))
            return topLines
        finally:
            openfile.close()

    def tailLines(self, lineNum):
        openfile = self.openFile('rU')
        try:
            lines = openfile.readlines()
            lineCount = len(lines)
            if (lineCount > lineNum):
                lines = lines[lineCount - lineNum :lineCount]
            topLines = []
            for line in lines :
                topLines.append(line.rstrip("\n"))
            return topLines
        finally:
            openfile.close()

    def lineCount(self):
        openfile = self.openFile('rU')
        try:
            return len(openfile.readlines())
        finally:
            openfile.close()
    """
    Write string in memory. Call flushXXX to save.
    """
    def write(self, content):
        self.memLines.append(content + "\n")

    def flushAndOverride(self):
        openfile = self.openFile('w')
        try:
            openfile.writelines(self.memLines)
            openfile.flush()
        finally:
            self.memLines = []
            openfile.close()

    def flushAndAppend(self):
        openfile = self.openFile('a')
        try:
            openfile.writelines(self.memLines)
            openfile.flush()
        finally:
            self.memLines = []
            openfile.close()
