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
    w.start();  #pwd607387

stock_code = sys.argv[1]
start_date = sys.argv[2]
end_date = sys.argv[3]
bar_size = sys.argv[4]
priceAdj = sys.argv[5]
fields = "open,close,high,low,volume,amt"
print "WindStockReuslt:"

#3.getKData
#分钟序列K线
# print w.wsi("000001.SZ", "open,close,high,low,volume,amt","2014-11-10 09:00:00", "2014-11-10 02:50:46", "")
# print w.wsi("000001.SZ", "open,close,high,low,volume,amt","2014-11-10 09:00:00", "2014-11-10 11:50:46", "BarSize=30")
# print w.wsd("000001.SZ", fields, "2014-11-17", "2014-11-20", "Fill=Previous")


res = w.wsi(stock_code,fields,start_date,end_date,"{0}{1}".format(bar_size, priceAdj))
print res
print "\n.Codes="+str1D(res.Codes)
print "\n.Fields="+str1D(res.Fields)
print "\n.Times="+str1D([ format(x,'%Y%m%d %H:%M:%S') for x in res.Times])
print "\n.Data="+str2D(res.Data)
