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
priceAdj = sys.argv[5]

#fields = "open,close,high,low,volume,amt"
# fields = "open,high,low,close,volume,amt,adjfactor,trade_status,susp_reason"
fields = "open,high,low,close,volume,amt,adjfactor"

if priceAdj == "N":
    priceAdj=""

# print aggregation


#3.getKData

res =  w.wsd(stock_code, fields, start_date, end_date, "{0}{1}Fill=Previous".format(aggregation, priceAdj))
if (res.ErrorCode == 0):
    resM5 = w.wsd(stock_code, "MA",start_date,end_date,"MA_N=5;{0}{1}Fill=Previous;".format(aggregation, priceAdj))
    print resM5
    resM10 = w.wsd(stock_code, "MA",start_date,end_date,"MA_N=10;{0}{1}Fill=Previous;".format(aggregation, priceAdj))
    print resM10
    resM20 = w.wsd(stock_code, "MA",start_date,end_date,"MA_N=20;{0}{1}Fill=Previous;".format(aggregation, priceAdj))
    print resM20
    if (resM5.ErrorCode == 0):
        resM5Data = resM5.Data[0]
        res.Fields = res.Fields + ['ma5']
        res.Data = res.Data + [resM5Data]
    if (resM10.ErrorCode == 0):
        resM10Data = resM10.Data[0]
        res.Fields = res.Fields + ['ma10']
        res.Data = res.Data + [resM10Data]
    if (resM20.ErrorCode == 0):
        resM20Data = resM20.Data[0]
        res.Fields = res.Fields + ['ma20']
        res.Data = res.Data + [resM20Data]

print "WindStockReuslt:"
print res
print "\n.Codes="+str1D(res.Codes)
print "\n.Fields="+str1D(res.Fields)
# print "\n.Times="+str1D([ format(x,'%Y%m%d') for x in res.Times])
print "\n.Times="+str1D([ format(x,'%Y%m%d %H:%M:%S') for x in res.Times])
print "\n.Data="+str2D(res.Data)


# print w.wsd(stock_code,fields,start_date,end_date,"Fill=Previous".aggregation)

