package com.edtest.devicetools;

public class GetBand {
    public static int freqToBand(int freq) {
        int band = 0;
        switch(freq) {
            //5GHZ BANDS
            case 5035:
                band = 7;
                break;
            case 5040:
                band = 8;
                break;
            case 5045:
                band = 9;
                break;
            case 5050:
                band = 11;
                break;
            case 5060:
                band = 12;
                break;
            case 5080:
                band = 16;
                break;
            case 5160:
                band = 32;
                break;
            case 5170:
                band = 34;
                break;
            case 5180:
                band = 36;
                break;
            case 5190:
                band = 38;
                break;
            case 5200:
                band = 40;
                break;
            case 5210:
                band = 42;
                break;
            case 5220:
                band = 44;
                break;
            case 5230:
                band = 46;
                break;
            case 5240:
                band = 48;
                break;
            case 5250:
                band = 50;
                break;
            case 5260:
                band = 52;
                break;
            case 5270:
                band = 54;
                break;
            case 5280:
                band = 56;
                break;
            case 5290:
                band = 58;
                break;
            case 5300:
                band = 60;
                break;
            case 5310:
                band = 62;
                break;
            case 5320:
                band = 64;
                break;
            case 5340:
                band = 68;
                break;
            case 5480:
                band = 94;
                break;
            case 5500:
                band = 100;
                break;
            case 5510:
                band = 102;
                break;
            case 5520:
                band = 104;
                break;
            case 5530:
                band = 106;
                break;
            case 5540:
                band = 108;
                break;
            case 5550:
                band = 110;
                break;
            case 5560:
                band = 112;
                break;
            case 5570:
                band = 114;
                break;
            case 5580:
                band = 116;
                break;
            case 5590:
                band = 118;
                break;
            case 5600:
                band = 120;
                break;
            case 5610:
                band = 122;
                break;
            case 5620:
                band = 124;
                break;
            case 5630:
                band = 126;
                break;
            case 5640:
                band = 128;
                break;
            case 5660:
                band = 132;
                break;
            case 5670:
                band = 134;
                break;
            case 5680:
                band = 136;
                break;
            case 5690:
                band = 138;
                break;
            case 5700:
                band = 140;
                break;
            case 5710:
                band = 142;
                break;
            case 5720:
                band = 144;
                break;
            case 5745:
                band = 149;
                break;
            case 5755:
                band = 151;
                break;
            case 5765:
                band = 153;
                break;
            case 5775:
                band = 155;
                break;
            case 5785:
                band = 157;
                break;
            case 5795:
                band = 159;
                break;
            case 5805:
                band = 161;
                break;
            case 5815:
                band = 163;
                break;
            case 5825:
                band = 165;
                break;
            case 5835:
                band = 167;
                break;
            case 5848:
                band = 169;
                break;
            case 5855:
                band = 171;
                break;
            case 5865:
                band = 173;
                break;
            case 5875:
                band = 175;
                break;
            case 5885:
                band = 177;
                break;
            case 4915:
                band = 183;
                break;
            case 4920:
                band = 185;
                break;
            case 4935:
                band = 187;
                break;
            case 4940:
                band = 188;
                break;
            case 4945:
                band = 189;
                break;
            case 4960:
                band = 192;
                break;
            case 4980:
                band = 196;
                break;

            //2.4GHZ BANDS
            case 2412:
                band = 1;
                break;
            case 2417:
                band = 2;
                break;
            case 2422:
                band = 3;
                break;
            case 2427:
                band = 4;
                break;
            case 2432:
                band = 5;
                break;
            case 2437:
                band = 6;
                break;
            case 2442:
                band = 7;
                break;
            case 2447:
                band = 8;
                break;
            case 2452:
                band = 9;
                break;
            case 2457:
                band = 10;
                break;
            case 2462:
                band = 11;
                break;
            case 2467:
                band = 12;
                break;
            case 2472:
                band = 13;
                break;
            case 2484:
                band = 14;
                break;
            default:
                band=0;
                break;
        }
        return band;
    }
}
