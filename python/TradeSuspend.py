######## coding=gbk
__author__ = 'henry'

import site
import sys

reload(sys)
sys.setdefaultencoding( "utf-8" )

from WindPy import w
from Utility import str1D
from Utility import str2D

if (w.isconnected() != True):
    w.start();  #pwd:607387

stock_code = sys.argv[1]
start_date = sys.argv[2]
end_date = sys.argv[3]

# res= w.wset("TradeSuspend","startdate=20150122;enddate=20150122")

fields = "sec_name,trade_status,susp_reason"
res =  w.wsd(stock_code, fields, start_date, end_date, "Fill=Previous")

#1.List getStocks
print "WindStockReuslt:"



print res
print "\n.Codes="+str1D(res.Codes)
print "\n.Fields="+str1D(res.Fields)
print "\n.Times="+str1D([ format(x,'%Y%m%d %H:%M:%S') for x in res.Times])
print "\n.Data="+str2D(res.Data)
