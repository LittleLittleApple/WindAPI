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

market_code = sys.argv[1]
#1.List getStocks
print "WindStockReuslt:"

# #2.getCurrentMarket
res= w.wsq(market_code, "rt_last,rt_pct_chg,rt_amt,rt_vol,rt_open,rt_high,rt_low")
print res
print "\n.Codes="+str1D(res.Codes)
print "\n.Fields="+str1D(res.Fields)
print "\n.Times="+str1D([ format(x,'%Y%m%d %H:%M:%S') for x in res.Times])
print "\n.Data="+str2D(res.Data)
