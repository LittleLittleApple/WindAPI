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
debug = None
if len(sys.argv) > 6 and sys.argv[6] == "debug":
    debug = True

fields = "open,close,high,low,volume,amt"

if priceAdj == "N":
    priceAdj=""

def is_same_day(strDateTime1, strDateTime2):
    strDate1 = strDateTime1.split(' ')[0]
    strDate2 = strDateTime2.split(' ')[0]
    return strDate1 == strDate2


# if not debug and (not is_same_day(start_date, end_date)):
#     err_msg = "Wrong parameters: the start date {0} is not equal to end date {1}. Same date is only supported.".format(start_date, end_date)
#     print "\n.ErrorCode=-99999"
#     print "\n.Data=[['{0}']]".format(err_msg)
#     raise StandardError, err_msg

#fetch and merge adj from wsd
res_day =  w.wsd(stock_code, "adjfactor", start_date, end_date, "Period=D;{0}Fill=Previous".format(priceAdj))
if res_day.ErrorCode != 0:
    print res_day
    err_msg = "Error while fetching Date K data.ErrorCode: {0}".format(res_day.ErrorCode)
    raise StandardError,err_msg

adjfactor=res_day.Data[0][0]

#3.getKData
#分钟序列K线
# print w.wsi("000001.SZ", "open,close,high,low,volume,amt","2014-11-10 09:00:00", "2014-11-10 02:50:46", "")
# print w.wsi("000001.SZ", "open,close,high,low,volume,amt","2014-11-10 09:00:00", "2014-11-10 11:50:46", "BarSize=30")
# print w.wsd("000001.SZ", fields, "2014-11-17", "2014-11-20", "Fill=Previous")


res = w.wsi(stock_code,fields,start_date,end_date,"{0}{1}Fill=Previous;".format(bar_size, priceAdj))
print res

#add adjfactor to kdata
adjfactor_lst = []    #temp adjfactor list.
for item in res.Times:
    adjfactor_lst = adjfactor_lst + [adjfactor]

res.Fields = res.Fields + ['adjfactor']
res.Data = res.Data + [adjfactor_lst]

#start to merge MA data
if res.ErrorCode == 0:
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
