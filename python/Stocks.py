######## coding=gbk
__author__ = 'henry'

import site
import sys
import types
reload(sys)
sys.setdefaultencoding( "utf-8" )

from WindPy import w
from Utility import str1D
from Utility import str2D

if (w.isconnected() != True):
    w.start();  #pwd:607387
date = sys.argv[1]
sector = sys.argv[2]
#1.List getStocks
print "WindStockReuslt:"
res= w.wset("SectorConstituent","date={0};sector={1}".format(date, sector))
print res
print "\n.Codes="+str1D(res.Codes)
print "\n.Fields="+str1D(res.Fields)
print "\n.Times="+str1D([ format(x,'%Y%m%d') for x in res.Times])
print "\n.Data="+str2D(res.Data)
