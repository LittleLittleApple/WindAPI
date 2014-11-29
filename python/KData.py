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
aggregation = sys.argv[4]
fields = "open,close,high,low,volume,amt"

# print aggregation
print "WindStockReuslt:"

#3.getKData
# res =  w.wsd("000001.SZ", fields, "2014-11-17", "2014-11-20", "{0}Fill=Previous".format(aggregation))
res =  w.wsd(stock_code, fields, start_date, end_date, "{0}Fill=Previous".format(aggregation))
print res
print "\n.Codes="+str1D(res.Codes)
print "\n.Fields="+str1D(res.Fields)
print "\n.Times="+str1D([ format(x,'%Y%m%d') for x in res.Times])
print "\n.Data="+str2D(res.Data)


# print w.wsd(stock_code,fields,start_date,end_date,"Fill=Previous".aggregation)

