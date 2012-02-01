'''
Created on Jan 30, 2012

@author: ehaijsu
'''

from intf import FileHandler
from util import TextFile
import os

class VmStatusHandler(FileHandler):
    def __init__(self):
        FileHandler.__init__(self)

    def processLine(self, line):
        arr = line.split()
        print arr
        return True

if __name__ == '__main__':
    dataFolder = "../../test/data/"
    files = os.listdir(dataFolder)
    processInfoTimes = []
    vmstatTimes = []
    for fileName in files:
        if (os.path.isfile(dataFolder + fileName)) :
            if (fileName.startswith("vmstat")) :
                vmstatTimes.append(fileName.split(".")[1])
            if (fileName.startswith("process_info")) :
                processInfoTimes.append(fileName.split(".")[1])
    availableTimes = list(set(processInfoTimes) & set(vmstatTimes))
    availableTimes.sort()
    firstFileName = "vmstat." + availableTimes[0]
    if (len(availableTimes) > 2):
        # there is gzip file
        firstFileName = firstFileName + ".gz"
    txtfile = TextFile(dataFolder + firstFileName)
    availableBeginDate = txtfile.readFirstLine()[0:8]
    print "Date range: " + availableBeginDate + " ~ " + availableTimes[len(availableTimes) - 1]
    startDate = raw_input("Please select start date [" + availableTimes[len(availableTimes) - 1] + "]:")
    timeInterval = raw_input("Please input the days [1]:")
    if (startDate == "") :
        startDate = availableTimes[len(availableTimes) - 1]
    if (timeInterval == "") :
        timeInterval = 1
    print "startDate = " + startDate
    print "Time interval = " + str(timeInterval)

