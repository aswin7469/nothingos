package com.google.zxing.pdf417.encoder;

import android.icu.lang.UCharacter;
import android.net.connectivity.android.net.mdns.aidl.IMDnsEventListener;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.google.zxing.WriterException;
import com.google.zxing.pdf417.PDF417Common;
import com.nothing.p023os.device.DeviceConstant;
import java.net.HttpURLConnection;
import javax.xml.datatype.DatatypeConstants;

final class PDF417ErrorCorrection {
    private static final int[][] EC_COEFFICIENTS = {new int[]{27, 917}, new int[]{522, 568, 723, 809}, new int[]{237, 308, 436, UCharacter.UnicodeBlock.GUNJALA_GONDI_ID, 646, 653, 428, 379}, new int[]{UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F_ID, 562, 232, 755, 599, 524, 801, 132, UCharacter.UnicodeBlock.NYIAKENG_PUACHUE_HMONG_ID, 116, 442, 428, UCharacter.UnicodeBlock.NYIAKENG_PUACHUE_HMONG_ID, 42, 176, 65}, new int[]{361, 575, 922, 525, 176, 586, 640, 321, 536, 742, 677, 742, 687, UCharacter.UnicodeBlock.GUNJALA_GONDI_ID, 193, 517, UCharacter.UnicodeBlock.TANGUT_COMPONENTS_ID, 494, 263, 147, 593, 800, 571, 320, 803, 133, UCharacter.UnicodeBlock.LATIN_EXTENDED_E_ID, 390, 685, 330, 63, HttpURLConnection.HTTP_GONE}, new int[]{539, 422, 6, 93, 862, 771, 453, 106, DeviceConstant.ORDER_ANC, UCharacter.UnicodeBlock.MAKASAR_ID, 107, HttpURLConnection.HTTP_VERSION, 733, 877, 381, IMDnsEventListener.SERVICE_GET_ADDR_SUCCESS, 723, 476, 462, 172, 430, 609, 858, 822, 543, 376, 511, 400, 672, 762, UCharacter.UnicodeBlock.GEORGIAN_EXTENDED_ID, 184, 440, 35, 519, 31, 460, 594, 225, 535, 517, SysUiStatsLog.SMARTSPACE_CARD_REPORTED, IMDnsEventListener.SERVICE_REGISTRATION_FAILED, 158, DeviceConstant.ORDER_GESTURE_CONTROLS, 201, 488, HttpURLConnection.HTTP_BAD_GATEWAY, 648, 733, 717, 83, HttpURLConnection.HTTP_NOT_FOUND, 97, 280, 771, DatatypeConstants.MIN_TIMEZONE_OFFSET, 629, 4, 381, 843, 623, 264, 543}, new int[]{521, UCharacter.UnicodeBlock.CYPRO_MINOAN_ID, 864, 547, 858, 580, UCharacter.UnicodeBlock.OTTOMAN_SIYAQ_NUMBERS_ID, 379, 53, 779, 897, 444, 400, 925, 749, HttpURLConnection.HTTP_UNSUPPORTED_TYPE, 822, 93, 217, 208, PDF417Common.MAX_CODEWORDS_IN_BARCODE, 244, 583, DeviceConstant.ORDER_EQUALIZER, 246, 148, 447, 631, UCharacter.UnicodeBlock.EGYPTIAN_HIEROGLYPH_FORMAT_CONTROLS_ID, 908, 490, 704, 516, 258, 457, 907, 594, 723, 674, UCharacter.UnicodeBlock.EGYPTIAN_HIEROGLYPH_FORMAT_CONTROLS_ID, 272, 96, 684, 432, 686, IMDnsEventListener.SERVICE_REGISTERED, 860, 569, 193, 219, 129, 186, 236, UCharacter.UnicodeBlock.MAKASAR_ID, 192, 775, UCharacter.UnicodeBlock.SOYOMBO_ID, 173, 40, 379, 712, 463, 646, 776, 171, 491, UCharacter.UnicodeBlock.SMALL_KANA_EXTENSION_ID, 763, 156, 732, 95, 270, 447, 90, 507, 48, 228, 821, 808, 898, 784, 663, 627, 378, 382, 262, 380, IMDnsEventListener.SERVICE_DISCOVERY_FAILED, 754, 336, 89, 614, 87, 432, DeviceConstant.ORDER_RIGHT_EARBUD, 616, 157, 374, 242, 726, 600, 269, 375, 898, 845, 454, 354, 130, 814, 587, 804, 34, 211, 330, 539, UCharacter.UnicodeBlock.SMALL_KANA_EXTENSION_ID, 827, 865, 37, 517, 834, UCharacter.UnicodeBlock.OLD_UYGHUR_ID, 550, 86, 801, 4, 108, 539}, new int[]{524, 894, 75, 766, 882, 857, 74, 204, 82, 586, 708, 250, 905, 786, 138, DeviceConstant.ORDER_FIND_DEVICE, 858, 194, UCharacter.UnicodeBlock.ETHIOPIC_EXTENDED_B_ID, 913, 275, 190, 375, 850, 438, 733, 194, 280, 201, 280, 828, 757, DeviceConstant.ORDER_IN_EAR_DETECTION, 814, 919, 89, 68, 569, 11, 204, 796, IMDnsEventListener.SERVICE_REGISTRATION_FAILED, 540, 913, 801, 700, 799, 137, 439, 418, 592, 668, 353, 859, 370, 694, 325, 240, 216, 257, UCharacter.UnicodeBlock.GUNJALA_GONDI_ID, 549, 209, 884, UCharacter.UnicodeBlock.OLD_UYGHUR_ID, 70, 329, 793, 490, UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F_ID, 877, 162, 749, 812, 684, 461, 334, 376, 849, 521, 307, UCharacter.UnicodeBlock.SOGDIAN_ID, 803, 712, 19, 358, 399, 908, 103, 511, 51, 8, 517, 225, UCharacter.UnicodeBlock.MEDEFAIDRIN_ID, 470, 637, 731, 66, 255, 917, 269, 463, 830, DeviceConstant.ORDER_SEE_MORE, 433, 848, 585, 136, 538, 906, 90, 2, UCharacter.UnicodeBlock.OLD_SOGDIAN_ID, 743, 199, 655, 903, 329, 49, 802, 580, 355, 588, 188, 462, 10, 134, 628, 320, 479, 130, 739, 71, 263, UCharacter.UnicodeBlock.UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED_A_ID, 374, DeviceConstant.ORDER_SOUND, 192, IMDnsEventListener.SERVICE_REGISTRATION_FAILED, 142, 673, 687, 234, 722, 384, 177, 752, IMDnsEventListener.SERVICE_RESOLUTION_FAILED, 640, 455, 193, 689, 707, 805, 641, 48, 60, 732, 621, 895, 544, 261, 852, 655, UCharacter.UnicodeBlock.ARABIC_EXTENDED_B_ID, 697, 755, 756, 60, UCharacter.UnicodeBlock.LATIN_EXTENDED_E_ID, 773, 434, 421, 726, 528, HttpURLConnection.HTTP_UNAVAILABLE, 118, 49, 795, 32, 144, 500, 238, 836, 394, 280, 566, UCharacter.UnicodeBlock.VITHKUQI_ID, 9, 647, 550, 73, 914, 342, 126, 32, 681, 331, 792, DeviceConstant.ORDER_EQUALIZER, 60, 609, 441, 180, 791, 893, 754, IMDnsEventListener.SERVICE_REGISTRATION_FAILED, 383, 228, 749, 760, 213, 54, UCharacter.UnicodeBlock.SMALL_KANA_EXTENSION_ID, 134, 54, 834, UCharacter.UnicodeBlock.TAMIL_SUPPLEMENT_ID, 922, 191, 910, 532, 609, 829, 189, 20, 167, 29, 872, 449, 83, 402, 41, 656, HttpURLConnection.HTTP_VERSION, 579, 481, 173, HttpURLConnection.HTTP_NOT_FOUND, 251, 688, 95, 497, 555, 642, 543, 307, 159, 924, 558, 648, 55, 497, 10}, new int[]{SysUiStatsLog.SMARTSPACE_CARD_REPORTED, 77, 373, HttpURLConnection.HTTP_GATEWAY_TIMEOUT, 35, 599, 428, 207, HttpURLConnection.HTTP_CONFLICT, 574, 118, 498, UCharacter.UnicodeBlock.HANIFI_ROHINGYA_ID, 380, 350, 492, 197, 265, 920, 155, 914, UCharacter.UnicodeBlock.TAMIL_SUPPLEMENT_ID, 229, 643, UCharacter.UnicodeBlock.NANDINAGARI_ID, 871, UCharacter.UnicodeBlock.SYMBOLS_FOR_LEGACY_COMPUTING_ID, 88, 87, 193, SysUiStatsLog.SMARTSPACE_CARD_REPORTED, 781, 846, 75, 327, 520, 435, 543, 203, 666, 249, 346, 781, 621, 640, 268, 794, 534, 539, 781, HttpURLConnection.HTTP_CLIENT_TIMEOUT, 390, 644, 102, 476, 499, UCharacter.UnicodeBlock.OLD_SOGDIAN_ID, 632, 545, 37, 858, 916, 552, 41, 542, UCharacter.UnicodeBlock.MEDEFAIDRIN_ID, 122, 272, 383, 800, 485, 98, 752, 472, 761, 107, 784, 860, 658, 741, UCharacter.UnicodeBlock.OLD_SOGDIAN_ID, 204, 681, HttpURLConnection.HTTP_PROXY_AUTH, 855, 85, 99, 62, 482, 180, 20, UCharacter.UnicodeBlock.SMALL_KANA_EXTENSION_ID, 451, 593, 913, 142, 808, 684, UCharacter.UnicodeBlock.MAKASAR_ID, 536, 561, 76, 653, 899, 729, 567, 744, 390, 513, 192, 516, 258, 240, 518, 794, 395, 768, 848, 51, DeviceConstant.ORDER_ANC, 384, 168, 190, 826, 328, 596, 786, 303, 570, 381, HttpURLConnection.HTTP_UNSUPPORTED_TYPE, 641, 156, 237, 151, 429, 531, 207, 676, DeviceConstant.ORDER_IN_EAR_DETECTION, 89, 168, 304, 402, 40, 708, 575, 162, 864, 229, 65, 861, 841, 512, 164, 477, 221, 92, 358, 785, UCharacter.UnicodeBlock.MAYAN_NUMERALS_ID, 357, 850, 836, 827, 736, 707, 94, 8, 494, 114, 521, 2, 499, 851, 543, 152, 729, 771, 95, 248, 361, 578, 323, 856, 797, UCharacter.UnicodeBlock.MEDEFAIDRIN_ID, 51, 684, SysUiStatsLog.MEDIAOUTPUT_OP_INTERACTION_REPORT, 533, 820, 669, 45, 902, 452, 167, 342, 244, 173, 35, 463, DeviceConstant.ORDER_GESTURE_CONTROLS, 51, 699, 591, 452, 578, 37, 124, UCharacter.UnicodeBlock.SYMBOLS_AND_PICTOGRAPHS_EXTENDED_A_ID, 332, 552, 43, 427, 119, 662, 777, 475, 850, 764, 364, 578, 911, UCharacter.UnicodeBlock.GEORGIAN_EXTENDED_ID, 711, 472, 420, 245, UCharacter.UnicodeBlock.MAYAN_NUMERALS_ID, 594, 394, 511, 327, 589, 777, 699, 688, 43, HttpURLConnection.HTTP_CLIENT_TIMEOUT, 842, 383, 721, 521, 560, 644, 714, 559, 62, 145, 873, 663, 713, 159, 672, 729, 624, 59, 193, 417, 158, 209, 563, 564, 343, 693, 109, IMDnsEventListener.SERVICE_RESOLVED, 563, 365, 181, 772, 677, UCharacter.UnicodeBlock.CYPRO_MINOAN_ID, 248, 353, 708, HttpURLConnection.HTTP_GONE, 579, 870, 617, 841, 632, 860, UCharacter.UnicodeBlock.MEDEFAIDRIN_ID, 536, 35, 777, 618, 586, 424, 833, 77, 597, 346, 269, 757, 632, 695, 751, 331, 247, 184, 45, 787, 680, 18, 66, HttpURLConnection.HTTP_PROXY_AUTH, 369, 54, 492, 228, 613, 830, 922, 437, 519, 644, 905, 789, 420, 305, 441, 207, 300, 892, 827, 141, 537, 381, 662, 513, 56, 252, 341, 242, 797, 838, 837, DeviceConstant.ORDER_FIND_DEVICE, 224, 307, 631, 61, 87, 560, UCharacter.UnicodeBlock.CYPRO_MINOAN_ID, 756, 665, 397, 808, 851, UCharacter.UnicodeBlock.ARABIC_EXTENDED_B_ID, 473, 795, 378, 31, 647, 915, 459, 806, 590, 731, 425, 216, 548, 249, 321, 881, 699, 535, 673, 782, 210, 815, 905, 303, 843, 922, 281, 73, 469, 791, DeviceConstant.ORDER_LEFT_EARBUD, 162, 498, 308, 155, 422, 907, 817, 187, 62, 16, 425, 535, 336, UCharacter.UnicodeBlock.INDIC_SIYAQ_NUMBERS_ID, 437, 375, UCharacter.UnicodeBlock.TANGUT_COMPONENTS_ID, DeviceConstant.ORDER_ANC, UCharacter.UnicodeBlock.OTTOMAN_SIYAQ_NUMBERS_ID, 183, 923, 116, StackStateAnimator.ANIMATION_DURATION_WAKEUP_SCRIM, 751, 353, 62, 366, 691, 379, 687, 842, 37, 357, DeviceConstant.ORDER_FIND_DEVICE, 742, 330, 5, 39, 923, UCharacter.UnicodeBlock.ETHIOPIC_EXTENDED_B_ID, 424, 242, 749, 321, 54, 669, UCharacter.UnicodeBlock.TANGSA_ID, 342, UCharacter.UnicodeBlock.TAMIL_SUPPLEMENT_ID, 534, 105, StackStateAnimator.ANIMATION_DURATION_WAKEUP_SCRIM, 488, 640, 672, 576, 540, UCharacter.UnicodeBlock.TANGSA_ID, 486, 721, DeviceConstant.ORDER_ANC, 46, 656, 447, 171, 616, StackStateAnimator.ANIMATION_DURATION_APPEAR_DISAPPEAR, 190, 531, UCharacter.UnicodeBlock.SMALL_KANA_EXTENSION_ID, 321, 762, 752, 533, 175, 134, 14, 381, 433, 717, 45, 111, 20, 596, UCharacter.UnicodeBlock.GUNJALA_GONDI_ID, 736, 138, 646, HttpURLConnection.HTTP_LENGTH_REQUIRED, 877, 669, 141, 919, 45, 780, HttpURLConnection.HTTP_PROXY_AUTH, 164, 332, 899, 165, 726, 600, 325, 498, 655, 357, 752, 768, 223, 849, 647, 63, UCharacter.UnicodeBlock.CYPRO_MINOAN_ID, 863, 251, 366, 304, 282, 738, 675, HttpURLConnection.HTTP_GONE, 389, 244, 31, 121, 303, 263}};

    private PDF417ErrorCorrection() {
    }

    static int getErrorCorrectionCodewordCount(int i) {
        if (i >= 0 && i <= 8) {
            return 1 << (i + 1);
        }
        throw new IllegalArgumentException("Error correction level must be between 0 and 8!");
    }

    static int getRecommendedMinimumErrorCorrectionLevel(int i) throws WriterException {
        if (i <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        } else if (i <= 40) {
            return 2;
        } else {
            if (i <= 160) {
                return 3;
            }
            if (i <= 320) {
                return 4;
            }
            if (i <= 863) {
                return 5;
            }
            throw new WriterException("No recommendation possible");
        }
    }

    static String generateErrorCorrection(CharSequence charSequence, int i) {
        int errorCorrectionCodewordCount = getErrorCorrectionCodewordCount(i);
        char[] cArr = new char[errorCorrectionCodewordCount];
        int length = charSequence.length();
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = errorCorrectionCodewordCount - 1;
            int charAt = (charSequence.charAt(i2) + cArr[i3]) % PDF417Common.NUMBER_OF_CODEWORDS;
            while (i3 >= 1) {
                cArr[i3] = (char) ((cArr[i3 - 1] + (929 - ((EC_COEFFICIENTS[i][i3] * charAt) % PDF417Common.NUMBER_OF_CODEWORDS))) % PDF417Common.NUMBER_OF_CODEWORDS);
                i3--;
            }
            cArr[0] = (char) ((929 - ((charAt * EC_COEFFICIENTS[i][0]) % PDF417Common.NUMBER_OF_CODEWORDS)) % PDF417Common.NUMBER_OF_CODEWORDS);
        }
        StringBuilder sb = new StringBuilder(errorCorrectionCodewordCount);
        for (int i4 = errorCorrectionCodewordCount - 1; i4 >= 0; i4--) {
            char c = cArr[i4];
            if (c != 0) {
                cArr[i4] = (char) (929 - c);
            }
            sb.append(cArr[i4]);
        }
        return sb.toString();
    }
}
