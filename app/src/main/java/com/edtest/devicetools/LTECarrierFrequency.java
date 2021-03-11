package com.edtest.devicetools;

import android.util.Log;

public class LTECarrierFrequency {
    public static double DOWNLINK_ONLY = 999999.0d;


    public static int getULEARFCN(int dlearfcn) {
        int ulearfcn = 0;
        //get band
        int band = getBand(dlearfcn);
        int baseDL = getBaseDLEARFCN(band);
        int offsetDL = dlearfcn - baseDL;
        int baseUL = getBaseULEARFCN(band);
        ulearfcn = baseUL + offsetDL;
        return ulearfcn;
    }

    public static double getDLFREQ(int dlearfcn) {
        double dlfreq = 0.0d;
        int band = getBand(dlearfcn);
        int baseDL = getBaseDLEARFCN(band);
        dlfreq = getFdl(band) + 0.1 * (dlearfcn - baseDL);
        return dlfreq;
    }

    public static double getULFREQ(int dlearfcn) {
        double ulfreq = 0.0d;
        int band = getBand(dlearfcn);
        int baseUL = getBaseULEARFCN(band);
        int ulearfcn = getULEARFCN(dlearfcn);
        ulfreq = getFul(band) + 0.1 * (ulearfcn - baseUL);
        return ulfreq;
    }

    public static double getFdl(int band) {
        switch (band) {
            case 1: return 2110;
            case 2: return 1930;
            case 3: return 1805;
            case 4: return 2110;
            case 5: return 869;
            case 7: return 2620;
            case 8: return 925;
            case 9: return 1844.9;
            case 10: return 2110;
            case 11: return 1475.9;
            case 12: return 729;
            case 13: return 746;
            case 14: return 758;
            case 17: return 734;
            case 18: return 860;
            case 19: return 875;
            case 20: return 791;
            case 21: return 1495.9;
            case 22: return 3510;
            case 24: return 1525;
            case 25: return 1930;
            case 26: return 859;
            case 27: return 852;
            case 28: return 758;
            case 29: return 717;
            case 30: return 2350;
            case 31: return 462.5;
            case 32: return 1452;
            case 33: return 1900;
            case 34: return 2010;
            case 35: return 1850;
            case 36: return 1930;
            case 37: return 1910;
            case 38: return 2570;
            case 39: return 1880;
            case 40: return 2300;
            case 41: return 2496;
            case 42: return 3400;
            case 43: return 3600;
            case 44: return 703;
            case 45: return 1447;
            case 46: return 5150;
            case 47: return 5855;
            case 48: return 3550;
            case 49: return 3550;
            case 50: return 1432;
            case 51: return 1427;
            case 52: return 3300;
            case 53: return 2483.5;
            case 65: return 2110;
            case 66: return 2110;
            case 67: return 738;
            case 68: return 753;
            case 69: return 2570;
            case 70: return 1995;
            case 71: return 617;
            case 72: return 461;
            case 73: return 460;
            case 74: return 1475;
            case 75: return 1432;
            case 76: return 1427;
            case 85: return 728;
            case 87: return 420;
            case 88: return 422;
        }
        return 0;
    }

    public static double getFul(int band) {
        switch (band) {
            case 1: return 1920;
            case 2: return 1850;
            case 3: return 1710;
            case 4: return 1710;
            case 5: return 824;
            case 7: return 2500;
            case 8: return 880;
            case 9: return 1749.9;
            case 10: return 1710;
            case 11: return 1427.9;
            case 12: return 699;
            case 13: return 777;
            case 14: return 788;
            case 17: return 704;
            case 18: return 815;
            case 19: return 830;
            case 20: return 832;
            case 21: return 1447.9;
            case 22: return 3410;
            case 24: return 1626.5;
            case 25: return 1850;
            case 26: return 814;
            case 27: return 807;
            case 28: return 703;
            case 29: return DOWNLINK_ONLY;
            case 30: return 2305;
            case 31: return 452.5;
            case 32: return DOWNLINK_ONLY;
            case 33: return 1900;
            case 34: return 2010;
            case 35: return 1850;
            case 36: return 1930;
            case 37: return 1910;
            case 38: return 2570;
            case 39: return 1880;
            case 40: return 2300;
            case 41: return 2496;
            case 42: return 3400;
            case 43: return 3600;
            case 44: return 703;
            case 45: return 1447;
            case 46: return 5150;
            case 47: return 5855;
            case 48: return 3550;
            case 49: return 3550;
            case 50: return 1432;
            case 51: return 1427;
            case 52: return 3300;
            case 53: return 2483.5;
            case 65: return 1920;
            case 66: return 1710;
            case 67: return DOWNLINK_ONLY;
            case 68: return 698;
            case 69: return DOWNLINK_ONLY;
            case 70: return 1695;
            case 71: return 663;
            case 72: return 451;
            case 73: return 450;
            case 74: return 1427;
            case 75: return DOWNLINK_ONLY;
            case 76: return DOWNLINK_ONLY;
            case 85: return 698;
            case 87: return 410;
            case 88: return 412;
        }
        return 0;
    }

    public static int getBand(int dlEARFCN) {
        if (isBetween(dlEARFCN,0,599)) {return 1;}
        if (isBetween(dlEARFCN,600,1199)) {return 2;}
        if (isBetween(dlEARFCN,1200,1949)) {return 3;}
        if (isBetween(dlEARFCN,1950,2399)) {return 4;}
        if (isBetween(dlEARFCN,2400,2649)) {return 5;}
        if (isBetween(dlEARFCN,2750,3449)) {return 7;}
        if (isBetween(dlEARFCN,3450,3799)) {return 8;}
        if (isBetween(dlEARFCN,3800,4149)) {return 9;}
        if (isBetween(dlEARFCN,4150,4749)) {return 10;}
        if (isBetween(dlEARFCN,4750,4949)) {return 11;}
        if (isBetween(dlEARFCN,5010,5179)) {return 12;}
        if (isBetween(dlEARFCN,5180,5279)) {return 13;}
        if (isBetween(dlEARFCN,5280,5379)) {return 14;}
        if (isBetween(dlEARFCN,5730,5849)) {return 17;}
        if (isBetween(dlEARFCN,5850,5999)) {return 18;}
        if (isBetween(dlEARFCN,6000,6149)) {return 19;}
        if (isBetween(dlEARFCN,6150,6449)) {return 20;}
        if (isBetween(dlEARFCN,6450,6599)) {return 21;}
        if (isBetween(dlEARFCN,6600,7399)) {return 22;}
        if (isBetween(dlEARFCN,7700,8039)) {return 24;}
        if (isBetween(dlEARFCN,8040,8689)) {return 25;}
        if (isBetween(dlEARFCN,8690,9039)) {return 26;}
        if (isBetween(dlEARFCN,9040,9209)) {return 27;}
        if (isBetween(dlEARFCN,9210,9659)) {return 28;}
        if (isBetween(dlEARFCN,9660,9769)) {return 29;}
        if (isBetween(dlEARFCN,9770,9869)) {return 30;}
        if (isBetween(dlEARFCN,9870,9919)) {return 31;}
        if (isBetween(dlEARFCN,9920,10359)) {return 32;}
        if (isBetween(dlEARFCN,36000,36199)) {return 33;}
        if (isBetween(dlEARFCN,36200,36349)) {return 34;}
        if (isBetween(dlEARFCN,36350,36949)) {return 35;}
        if (isBetween(dlEARFCN,36950,37549)) {return 36;}
        if (isBetween(dlEARFCN,37550,37749)) {return 37;}
        if (isBetween(dlEARFCN,37750,38249)) {return 38;}
        if (isBetween(dlEARFCN,38250,38649)) {return 39;}
        if (isBetween(dlEARFCN,38650,39649)) {return 40;}
        if (isBetween(dlEARFCN,39650,41589)) {return 41;}
        if (isBetween(dlEARFCN,41590,43589)) {return 42;}
        if (isBetween(dlEARFCN,43590,45589)) {return 43;}
        if (isBetween(dlEARFCN,45590,46589)) {return 44;}
        if (isBetween(dlEARFCN,46590,46789)) {return 45;}
        if (isBetween(dlEARFCN,46790,54539)) {return 46;}
        if (isBetween(dlEARFCN,54540,55239)) {return 47;}
        if (isBetween(dlEARFCN,55240,56739)) {return 48;}
        if (isBetween(dlEARFCN,56740,58239)) {return 49;}
        if (isBetween(dlEARFCN,58240,59089)) {return 50;}
        if (isBetween(dlEARFCN,59090,59139)) {return 51;}
        if (isBetween(dlEARFCN,59140,60139)) {return 52;}
        if (isBetween(dlEARFCN,60140,60254)) {return 53;}
        if (isBetween(dlEARFCN,65536,66435)) {return 65;}
        if (isBetween(dlEARFCN,66436,67335)) {return 66;}
        if (isBetween(dlEARFCN,67336,67535)) {return 67;}
        if (isBetween(dlEARFCN,67536,67835)) {return 68;}
        if (isBetween(dlEARFCN,67836,68335)) {return 69;}
        if (isBetween(dlEARFCN,68336,68585)) {return 70;}
        if (isBetween(dlEARFCN,68586,68935)) {return 71;}
        if (isBetween(dlEARFCN,68936,68985)) {return 72;}
        if (isBetween(dlEARFCN,68986,69035)) {return 73;}
        if (isBetween(dlEARFCN,69036,69465)) {return 74;}
        if (isBetween(dlEARFCN,69466,70315)) {return 75;}
        if (isBetween(dlEARFCN,70316,70365)) {return 76;}
        if (isBetween(dlEARFCN,70366,70545)) {return 85;}
        if (isBetween(dlEARFCN,70546,70595)) {return 87;}
        if (isBetween(dlEARFCN,70596,70645)) {return 88;}
        return 0;
    }

    public static int getBaseDLEARFCN(int band) {
        switch (band) {
            case 1: return 0;
            case 2: return 600;
            case 3: return 1200;
            case 4: return 1950;
            case 5: return 2400;
            case 7: return 2750;
            case 8: return 3450;
            case 9: return 3800;
            case 10: return 4150;
            case 11: return 4750;
            case 12: return 5010;
            case 13: return 5180;
            case 14: return 5280;
            case 17: return 5730;
            case 18: return 5850;
            case 19: return 6000;
            case 20: return 6150;
            case 21: return 6450;
            case 22: return 6600;
            case 24: return 7700;
            case 25: return 8040;
            case 26: return 8690;
            case 27: return 9040;
            case 28: return 9210;
            case 29: return 9660;
            case 30: return 9770;
            case 31: return 9870;
            case 32: return 9920;
            case 33: return 36000;
            case 34: return 36200;
            case 35: return 36350;
            case 36: return 36950;
            case 37: return 37550;
            case 38: return 37750;
            case 39: return 38250;
            case 40: return 38650;
            case 41: return 39650;
            case 42: return 41590;
            case 43: return 43590;
            case 44: return 45590;
            case 45: return 46590;
            case 46: return 46790;
            case 47: return 54540;
            case 48: return 55240;
            case 49: return 56740;
            case 50: return 58240;
            case 51: return 59090;
            case 52: return 59140;
            case 53: return 60140;
            case 65: return 65536;
            case 66: return 66436;
            case 67: return 67336;
            case 68: return 67536;
            case 69: return 67836;
            case 70: return 68336;
            case 71: return 68586;
            case 72: return 68936;
            case 73: return 68986;
            case 74: return 69036;
            case 75: return 69466;
            case 76: return 70316;
            case 85: return 70366;
            case 87: return 70546;
            case 88: return 70596;
        }
        return 0;
    }

    public static int getBaseULEARFCN(int band) {
        switch (band) {
            case 1: return 18000;
            case 2: return 18600;
            case 3: return 19200;
            case 4: return 19950;
            case 5: return 20400;
            case 7: return 20750;
            case 8: return 21450;
            case 9: return 21800;
            case 10: return 22150;
            case 11: return 22750;
            case 12: return 23010;
            case 13: return 23180;
            case 14: return 23280;
            case 17: return 23730;
            case 18: return 23850;
            case 19: return 24000;
            case 20: return 24150;
            case 21: return 24450;
            case 22: return 24600;
            case 24: return 25700;
            case 25: return 26040;
            case 26: return 26690;
            case 27: return 27040;
            case 28: return 27210;
            case 29: return (int) DOWNLINK_ONLY;
            case 30: return 27660;
            case 31: return 27760;
            case 32: return (int) DOWNLINK_ONLY;
            case 33: return 36000;
            case 34: return 36200;
            case 35: return 36350;
            case 36: return 36950;
            case 37: return 37550;
            case 38: return 37750;
            case 39: return 38250;
            case 40: return 38650;
            case 41: return 39650;
            case 42: return 41590;
            case 43: return 43590;
            case 44: return 45590;
            case 45: return 46590;
            case 46: return 46790;
            case 47: return 54540;
            case 48: return 55240;
            case 49: return 56740;
            case 50: return 58240;
            case 51: return 59090;
            case 52: return 59140;
            case 53: return 60140;
            case 65: return 131072;
            case 66: return 131972;
            case 67: return (int) DOWNLINK_ONLY;
            case 68: return 132672;
            case 69: return (int) DOWNLINK_ONLY;
            case 70: return 132972;
            case 71: return 133122;
            case 72: return 133472;
            case 73: return 133522;
            case 74: return 133572;
            case 75: return (int) DOWNLINK_ONLY;
            case 76: return (int) DOWNLINK_ONLY;
            case 85: return 134002;
            case 87: return 134182;
            case 88: return 134232;
        }
        return 0;
    }

    public static String getBandName(int band) {
        switch (band) {
            case 1: return "2100";
            case 2: return "1900 PCS";
            case 3: return "1800+";
            case 4: return "AWS-1";
            case 5: return "850";
            case 7: return "2600";
            case 8: return "900 GSM";
            case 9: return "1800";
            case 10: return "AWS-1+";
            case 11: return "1500 Lower";
            case 12: return "700-A";
            case 13: return "700-C";
            case 14: return "700-PS";
            case 17: return "700-B";
            case 18: return "800 Lower";
            case 19: return "800 Upper";
            case 20: return "800 DD";
            case 21: return "1500 Upper";
            case 22: return "3500";
            case 24: return "1600 L-band";
            case 25: return "1900+";
            case 26: return "850+";
            case 27: return "800 SMR";
            case 28: return "700 APT";
            case 29: return "700-D";
            case 30: return "2300 WCS";
            case 31: return "450";
            case 32: return "1500 L-band";
            case 33: return "TD 1900";
            case 34: return "TD 2000";
            case 35: return "TD PCS Lower";
            case 36: return "TD PCS Upper";
            case 37: return "TD PCS Center gap";
            case 38: return "TD 2600";
            case 39: return "TD 1900+";
            case 40: return "TD 2300";
            case 41: return "TD 2600+";
            case 42: return "TD 3500";
            case 43: return "TD 3700";
            case 44: return "TD 700";
            case 45: return "TD 1500";
            case 46: return "TD Unlicensed";
            case 47: return "TD V2X";
            case 48: return "TD 3600";
            case 49: return "TD 3600r";
            case 50: return "TD 1500+";
            case 51: return "TD 1500-";
            case 52: return "TD 3300";
            case 53: return "TD 2500";
            case 65: return "2100+";
            case 66: return "AWS-3";
            case 67: return "700 EU";
            case 68: return "700 ME";
            case 69: return "DL B38";
            case 70: return "AWS-4";
            case 71: return "600";
            case 72: return "450 PMR/PAMR";
            case 73: return "450 APAC";
            case 74: return "L-band";
            case 75: return "DL B50";
            case 76: return "DL B51";
            case 85: return "700-A+";
            case 87: return "410";
            case 88: return "410+";
        }
        return "unknown";
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}
