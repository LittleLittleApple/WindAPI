######## coding=gbk
__author__ = 'henry'

import site
import sys
import math

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

if priceAdj == "N":
    priceAdj=""
#3.getKData
#��������K��
# print w.wsi("000001.SZ", "open,close,high,low,volume,amt","2014-11-10 09:00:00", "2014-11-10 02:50:46", "")
# print w.wsi("000001.SZ", "open,close,high,low,volume,amt","2014-11-10 09:00:00", "2014-11-10 11:50:46", "BarSize=30")
# print w.wsd("000001.SZ", fields, "2014-11-17", "2014-11-20", "Fill=Previous")


res = w.wsi(stock_code,fields,start_date,end_date,"{0}{1}Fill=Previous;".format(bar_size, priceAdj))
print res
if (res.ErrorCode == 0):
    resM5 = w.wsi(stock_code, "MA",start_date,end_date,"{0}{1}Fill=Previous;MA_N=5;".format(bar_size, priceAdj))
    print resM5
    resM10 = w.wsi(stock_code, "MA",start_date,end_date,"{0}{1}Fill=Previous;MA_N=10;".format(bar_size, priceAdj))
    print resM10
    resM20 = w.wsi(stock_code, "MA",start_date,end_date,"{0}{1}Fill=Previous;MA_N=20;".format(bar_size, priceAdj))
    print resM20
    if (resM5.ErrorCode == 0):
        resM5Data = resM5.Data[2]
        res.Fields = res.Fields + ['ma5']
        res.Data = res.Data + [resM5Data]
    if (resM10.ErrorCode == 0):
        resM10Data = resM10.Data[2]
        res.Fields = res.Fields + ['ma10']
        res.Data = res.Data + [resM10Data]
    if (resM20.ErrorCode == 0):
        resM20Data = resM20.Data[2]
        res.Fields = res.Fields + ['ma20']
        res.Data = res.Data + [resM20Data]

print "WindStockReuslt:"
print res
print "\n.Codes="+str1D(res.Codes)
print "\n.Fields="+str1D(res.Fields)
print "\n.Times="+str1D([ format(x,'%Y%m%d %H:%M:%S') for x in res.Times])
print "\n.Data="+str2D(res.Data)
