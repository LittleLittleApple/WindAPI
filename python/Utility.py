######## coding=gbk
import sys

def str1D(v1d):
                outLen = len(v1d);
                if(outLen==0):
                    return '[]';
                outdot = 0;
                outx='';
                outr='[';
                if outLen>10000 :
                    outLen = 10;
                    outdot = 1;

                for x in v1d[0:outLen]:
                   outr = outr + outx + str(x);
                   outx=',';

                if outdot>0 :
                    outr = outr + outx + '...';
                outr = outr + ']';
                return outr;
def str2D(v2d):
                outLen = len(v2d);
                if(outLen==0):
                    return '[]';
                outdot = 0;
                outx='';
                outr='[';
                if outLen>10000 :
                    outLen = 10;
                    outdot = 1;

                for x in v2d[0:outLen]:
                   outr = outr + outx + str1D(x);
                   outx=',';

                if outdot>0 :
                    outr = outr + outx + '...';
                outr = outr + ']';
                return outr;
