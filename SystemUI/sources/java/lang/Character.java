package java.lang;

import android.icu.lang.UCharacter;
import android.icu.text.DateFormat;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.net.wifi.WifiEnterpriseConfig;
import android.safetycenter.SafetyCenterEntry;
import androidx.core.view.MotionEventCompat;
import com.android.settingslib.datetime.ZoneGetter;
import java.p026io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class Character implements Serializable, Comparable<Character> {
    public static final int BYTES = 2;
    public static final byte COMBINING_SPACING_MARK = 8;
    public static final byte CONNECTOR_PUNCTUATION = 23;
    public static final byte CONTROL = 15;
    public static final byte CURRENCY_SYMBOL = 26;
    public static final byte DASH_PUNCTUATION = 20;
    public static final byte DECIMAL_DIGIT_NUMBER = 9;
    private static final byte[] DIRECTIONALITY = {0, 1, 3, 4, 5, 6, 7, 10, 11, 12, 13, 14, 15, 2, 16, 17, 18, 8, 9};
    public static final byte DIRECTIONALITY_ARABIC_NUMBER = 6;
    public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 9;
    public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 7;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 3;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 4;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 5;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 14;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 15;
    public static final byte DIRECTIONALITY_NONSPACING_MARK = 8;
    public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 13;
    public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 10;
    public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 18;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 2;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 16;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 17;
    public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 11;
    public static final byte DIRECTIONALITY_UNDEFINED = -1;
    public static final byte DIRECTIONALITY_WHITESPACE = 12;
    public static final byte ENCLOSING_MARK = 7;
    public static final byte END_PUNCTUATION = 22;
    static final int ERROR = -1;
    public static final byte FINAL_QUOTE_PUNCTUATION = 30;
    public static final byte FORMAT = 16;
    public static final byte INITIAL_QUOTE_PUNCTUATION = 29;
    public static final byte LETTER_NUMBER = 10;
    public static final byte LINE_SEPARATOR = 13;
    public static final byte LOWERCASE_LETTER = 2;
    public static final byte MATH_SYMBOL = 25;
    public static final int MAX_CODE_POINT = 1114111;
    public static final char MAX_HIGH_SURROGATE = '?';
    public static final char MAX_LOW_SURROGATE = '?';
    public static final int MAX_RADIX = 36;
    public static final char MAX_SURROGATE = '?';
    public static final char MAX_VALUE = '￿';
    public static final int MIN_CODE_POINT = 0;
    public static final char MIN_HIGH_SURROGATE = '?';
    public static final char MIN_LOW_SURROGATE = '?';
    public static final int MIN_RADIX = 2;
    public static final int MIN_SUPPLEMENTARY_CODE_POINT = 65536;
    public static final char MIN_SURROGATE = '?';
    public static final char MIN_VALUE = '\u0000';
    public static final byte MODIFIER_LETTER = 4;
    public static final byte MODIFIER_SYMBOL = 27;
    public static final byte NON_SPACING_MARK = 6;
    public static final byte OTHER_LETTER = 5;
    public static final byte OTHER_NUMBER = 11;
    public static final byte OTHER_PUNCTUATION = 24;
    public static final byte OTHER_SYMBOL = 28;
    public static final byte PARAGRAPH_SEPARATOR = 14;
    public static final byte PRIVATE_USE = 18;
    public static final int SIZE = 16;
    public static final byte SPACE_SEPARATOR = 12;
    public static final byte START_PUNCTUATION = 21;
    public static final byte SURROGATE = 19;
    public static final byte TITLECASE_LETTER = 3;
    public static final Class<Character> TYPE = Class.getPrimitiveClass("char");
    public static final byte UNASSIGNED = 0;
    public static final byte UPPERCASE_LETTER = 1;
    private static final long serialVersionUID = 3786198910865385080L;
    private final char value;

    public static int charCount(int i) {
        return i >= 65536 ? 2 : 1;
    }

    public static int compare(char c, char c2) {
        return c - c2;
    }

    static native int digitImpl(int i, int i2);

    public static char forDigit(int i, int i2) {
        if (i >= i2 || i < 0 || i2 < 2 || i2 > 36) {
            return 0;
        }
        return (char) (i < 10 ? i + 48 : i + 87);
    }

    static native byte getDirectionalityImpl(int i);

    private static native String getNameImpl(int i);

    static native int getNumericValueImpl(int i);

    static native int getTypeImpl(int i);

    public static int hashCode(char c) {
        return c;
    }

    public static char highSurrogate(int i) {
        return (char) ((i >>> 10) + 55232);
    }

    static native boolean isAlphabeticImpl(int i);

    public static boolean isBmpCodePoint(int i) {
        return (i >>> 16) == 0;
    }

    static native boolean isDefinedImpl(int i);

    static native boolean isDigitImpl(int i);

    public static boolean isHighSurrogate(char c) {
        return c >= 55296 && c < 56320;
    }

    public static boolean isISOControl(int i) {
        return i <= 159 && (i >= 127 || (i >>> 5) == 0);
    }

    static native boolean isIdentifierIgnorableImpl(int i);

    static native boolean isIdeographicImpl(int i);

    static native boolean isLetterImpl(int i);

    static native boolean isLetterOrDigitImpl(int i);

    public static boolean isLowSurrogate(char c) {
        return c >= 56320 && c < 57344;
    }

    static native boolean isLowerCaseImpl(int i);

    static native boolean isMirroredImpl(int i);

    @Deprecated
    public static boolean isSpace(char c) {
        return c <= ' ' && ((13824 >> c) & 1) != 0;
    }

    static native boolean isSpaceCharImpl(int i);

    public static boolean isSupplementaryCodePoint(int i) {
        return i >= 65536 && i < 1114112;
    }

    public static boolean isSurrogate(char c) {
        return c >= 55296 && c < 57344;
    }

    static native boolean isTitleCaseImpl(int i);

    static native boolean isUnicodeIdentifierPartImpl(int i);

    static native boolean isUnicodeIdentifierStartImpl(int i);

    static native boolean isUpperCaseImpl(int i);

    public static boolean isValidCodePoint(int i) {
        return (i >>> 16) < 17;
    }

    static native boolean isWhitespaceImpl(int i);

    public static char lowSurrogate(int i) {
        return (char) ((i & 1023) + 56320);
    }

    public static char reverseBytes(char c) {
        return (char) ((c << 8) | ((65280 & c) >> 8));
    }

    public static int toCodePoint(char c, char c2) {
        return ((c << 10) + c2) - 56613888;
    }

    static native int toLowerCaseImpl(int i);

    static native int toTitleCaseImpl(int i);

    static native int toUpperCaseImpl(int i);

    public static class Subset {
        private String name;

        public final boolean equals(Object obj) {
            return this == obj;
        }

        protected Subset(String str) {
            if (str != null) {
                this.name = str;
                return;
            }
            throw new NullPointerException(ZoneGetter.KEY_DISPLAYNAME);
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final String toString() {
            return this.name;
        }
    }

    public static final class UnicodeBlock extends Subset {
        public static final UnicodeBlock AEGEAN_NUMBERS;
        public static final UnicodeBlock ALCHEMICAL_SYMBOLS;
        public static final UnicodeBlock ALPHABETIC_PRESENTATION_FORMS;
        public static final UnicodeBlock ANCIENT_GREEK_MUSICAL_NOTATION;
        public static final UnicodeBlock ANCIENT_GREEK_NUMBERS;
        public static final UnicodeBlock ANCIENT_SYMBOLS;
        public static final UnicodeBlock ARABIC;
        public static final UnicodeBlock ARABIC_EXTENDED_A;
        public static final UnicodeBlock ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS;
        public static final UnicodeBlock ARABIC_PRESENTATION_FORMS_A;
        public static final UnicodeBlock ARABIC_PRESENTATION_FORMS_B;
        public static final UnicodeBlock ARABIC_SUPPLEMENT;
        public static final UnicodeBlock ARMENIAN;
        public static final UnicodeBlock ARROWS;
        public static final UnicodeBlock AVESTAN;
        public static final UnicodeBlock BALINESE;
        public static final UnicodeBlock BAMUM;
        public static final UnicodeBlock BAMUM_SUPPLEMENT;
        public static final UnicodeBlock BASIC_LATIN;
        public static final UnicodeBlock BATAK;
        public static final UnicodeBlock BENGALI;
        public static final UnicodeBlock BLOCK_ELEMENTS;
        public static final UnicodeBlock BOPOMOFO;
        public static final UnicodeBlock BOPOMOFO_EXTENDED;
        public static final UnicodeBlock BOX_DRAWING;
        public static final UnicodeBlock BRAHMI;
        public static final UnicodeBlock BRAILLE_PATTERNS;
        public static final UnicodeBlock BUGINESE;
        public static final UnicodeBlock BUHID;
        public static final UnicodeBlock BYZANTINE_MUSICAL_SYMBOLS;
        public static final UnicodeBlock CARIAN;
        public static final UnicodeBlock CHAKMA;
        public static final UnicodeBlock CHAM;
        public static final UnicodeBlock CHEROKEE;
        public static final UnicodeBlock CJK_COMPATIBILITY;
        public static final UnicodeBlock CJK_COMPATIBILITY_FORMS;
        public static final UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS;
        public static final UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT;
        public static final UnicodeBlock CJK_RADICALS_SUPPLEMENT;
        public static final UnicodeBlock CJK_STROKES;
        public static final UnicodeBlock CJK_SYMBOLS_AND_PUNCTUATION;
        public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS;
        public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A;
        public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B;
        public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C;
        public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D;
        public static final UnicodeBlock COMBINING_DIACRITICAL_MARKS;
        public static final UnicodeBlock COMBINING_DIACRITICAL_MARKS_SUPPLEMENT;
        public static final UnicodeBlock COMBINING_HALF_MARKS;
        public static final UnicodeBlock COMBINING_MARKS_FOR_SYMBOLS;
        public static final UnicodeBlock COMMON_INDIC_NUMBER_FORMS;
        public static final UnicodeBlock CONTROL_PICTURES;
        public static final UnicodeBlock COPTIC;
        public static final UnicodeBlock COUNTING_ROD_NUMERALS;
        public static final UnicodeBlock CUNEIFORM;
        public static final UnicodeBlock CUNEIFORM_NUMBERS_AND_PUNCTUATION;
        public static final UnicodeBlock CURRENCY_SYMBOLS;
        public static final UnicodeBlock CYPRIOT_SYLLABARY;
        public static final UnicodeBlock CYRILLIC;
        public static final UnicodeBlock CYRILLIC_EXTENDED_A;
        public static final UnicodeBlock CYRILLIC_EXTENDED_B;
        public static final UnicodeBlock CYRILLIC_SUPPLEMENTARY;
        public static final UnicodeBlock DESERET;
        public static final UnicodeBlock DEVANAGARI;
        public static final UnicodeBlock DEVANAGARI_EXTENDED;
        public static final UnicodeBlock DINGBATS;
        public static final UnicodeBlock DOMINO_TILES;
        public static final UnicodeBlock EGYPTIAN_HIEROGLYPHS;
        public static final UnicodeBlock EMOTICONS;
        public static final UnicodeBlock ENCLOSED_ALPHANUMERICS;
        public static final UnicodeBlock ENCLOSED_ALPHANUMERIC_SUPPLEMENT;
        public static final UnicodeBlock ENCLOSED_CJK_LETTERS_AND_MONTHS;
        public static final UnicodeBlock ENCLOSED_IDEOGRAPHIC_SUPPLEMENT;
        public static final UnicodeBlock ETHIOPIC;
        public static final UnicodeBlock ETHIOPIC_EXTENDED;
        public static final UnicodeBlock ETHIOPIC_EXTENDED_A;
        public static final UnicodeBlock ETHIOPIC_SUPPLEMENT;
        public static final UnicodeBlock GENERAL_PUNCTUATION;
        public static final UnicodeBlock GEOMETRIC_SHAPES;
        public static final UnicodeBlock GEORGIAN;
        public static final UnicodeBlock GEORGIAN_SUPPLEMENT;
        public static final UnicodeBlock GLAGOLITIC;
        public static final UnicodeBlock GOTHIC;
        public static final UnicodeBlock GREEK;
        public static final UnicodeBlock GREEK_EXTENDED;
        public static final UnicodeBlock GUJARATI;
        public static final UnicodeBlock GURMUKHI;
        public static final UnicodeBlock HALFWIDTH_AND_FULLWIDTH_FORMS;
        public static final UnicodeBlock HANGUL_COMPATIBILITY_JAMO;
        public static final UnicodeBlock HANGUL_JAMO;
        public static final UnicodeBlock HANGUL_JAMO_EXTENDED_A;
        public static final UnicodeBlock HANGUL_JAMO_EXTENDED_B;
        public static final UnicodeBlock HANGUL_SYLLABLES;
        public static final UnicodeBlock HANUNOO;
        public static final UnicodeBlock HEBREW;
        public static final UnicodeBlock HIGH_PRIVATE_USE_SURROGATES;
        public static final UnicodeBlock HIGH_SURROGATES;
        public static final UnicodeBlock HIRAGANA;
        public static final UnicodeBlock IDEOGRAPHIC_DESCRIPTION_CHARACTERS;
        public static final UnicodeBlock IMPERIAL_ARAMAIC;
        public static final UnicodeBlock INSCRIPTIONAL_PAHLAVI;
        public static final UnicodeBlock INSCRIPTIONAL_PARTHIAN;
        public static final UnicodeBlock IPA_EXTENSIONS;
        public static final UnicodeBlock JAVANESE;
        public static final UnicodeBlock KAITHI;
        public static final UnicodeBlock KANA_SUPPLEMENT;
        public static final UnicodeBlock KANBUN;
        public static final UnicodeBlock KANGXI_RADICALS;
        public static final UnicodeBlock KANNADA;
        public static final UnicodeBlock KATAKANA;
        public static final UnicodeBlock KATAKANA_PHONETIC_EXTENSIONS;
        public static final UnicodeBlock KAYAH_LI;
        public static final UnicodeBlock KHAROSHTHI;
        public static final UnicodeBlock KHMER;
        public static final UnicodeBlock KHMER_SYMBOLS;
        public static final UnicodeBlock LAO;
        public static final UnicodeBlock LATIN_1_SUPPLEMENT;
        public static final UnicodeBlock LATIN_EXTENDED_A;
        public static final UnicodeBlock LATIN_EXTENDED_ADDITIONAL;
        public static final UnicodeBlock LATIN_EXTENDED_B;
        public static final UnicodeBlock LATIN_EXTENDED_C;
        public static final UnicodeBlock LATIN_EXTENDED_D;
        public static final UnicodeBlock LEPCHA;
        public static final UnicodeBlock LETTERLIKE_SYMBOLS;
        public static final UnicodeBlock LIMBU;
        public static final UnicodeBlock LINEAR_B_IDEOGRAMS;
        public static final UnicodeBlock LINEAR_B_SYLLABARY;
        public static final UnicodeBlock LISU;
        public static final UnicodeBlock LOW_SURROGATES;
        public static final UnicodeBlock LYCIAN;
        public static final UnicodeBlock LYDIAN;
        public static final UnicodeBlock MAHJONG_TILES;
        public static final UnicodeBlock MALAYALAM;
        public static final UnicodeBlock MANDAIC;
        public static final UnicodeBlock MATHEMATICAL_ALPHANUMERIC_SYMBOLS;
        public static final UnicodeBlock MATHEMATICAL_OPERATORS;
        public static final UnicodeBlock MEETEI_MAYEK;
        public static final UnicodeBlock MEETEI_MAYEK_EXTENSIONS;
        public static final UnicodeBlock MEROITIC_CURSIVE;
        public static final UnicodeBlock MEROITIC_HIEROGLYPHS;
        public static final UnicodeBlock MIAO;
        public static final UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A;
        public static final UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B;
        public static final UnicodeBlock MISCELLANEOUS_SYMBOLS;
        public static final UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_ARROWS;
        public static final UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS;
        public static final UnicodeBlock MISCELLANEOUS_TECHNICAL;
        public static final UnicodeBlock MODIFIER_TONE_LETTERS;
        public static final UnicodeBlock MONGOLIAN;
        public static final UnicodeBlock MUSICAL_SYMBOLS;
        public static final UnicodeBlock MYANMAR;
        public static final UnicodeBlock MYANMAR_EXTENDED_A;
        public static final UnicodeBlock NEW_TAI_LUE;
        public static final UnicodeBlock NKO;
        public static final UnicodeBlock NUMBER_FORMS;
        public static final UnicodeBlock OGHAM;
        public static final UnicodeBlock OLD_ITALIC;
        public static final UnicodeBlock OLD_PERSIAN;
        public static final UnicodeBlock OLD_SOUTH_ARABIAN;
        public static final UnicodeBlock OLD_TURKIC;
        public static final UnicodeBlock OL_CHIKI;
        public static final UnicodeBlock OPTICAL_CHARACTER_RECOGNITION;
        public static final UnicodeBlock ORIYA;
        public static final UnicodeBlock OSMANYA;
        public static final UnicodeBlock PHAGS_PA;
        public static final UnicodeBlock PHAISTOS_DISC;
        public static final UnicodeBlock PHOENICIAN;
        public static final UnicodeBlock PHONETIC_EXTENSIONS;
        public static final UnicodeBlock PHONETIC_EXTENSIONS_SUPPLEMENT;
        public static final UnicodeBlock PLAYING_CARDS;
        public static final UnicodeBlock PRIVATE_USE_AREA;
        public static final UnicodeBlock REJANG;
        public static final UnicodeBlock RUMI_NUMERAL_SYMBOLS;
        public static final UnicodeBlock RUNIC;
        public static final UnicodeBlock SAMARITAN;
        public static final UnicodeBlock SAURASHTRA;
        public static final UnicodeBlock SHARADA;
        public static final UnicodeBlock SHAVIAN;
        public static final UnicodeBlock SINHALA;
        public static final UnicodeBlock SMALL_FORM_VARIANTS;
        public static final UnicodeBlock SORA_SOMPENG;
        public static final UnicodeBlock SPACING_MODIFIER_LETTERS;
        public static final UnicodeBlock SPECIALS;
        public static final UnicodeBlock SUNDANESE;
        public static final UnicodeBlock SUNDANESE_SUPPLEMENT;
        public static final UnicodeBlock SUPERSCRIPTS_AND_SUBSCRIPTS;
        public static final UnicodeBlock SUPPLEMENTAL_ARROWS_A;
        public static final UnicodeBlock SUPPLEMENTAL_ARROWS_B;
        public static final UnicodeBlock SUPPLEMENTAL_MATHEMATICAL_OPERATORS;
        public static final UnicodeBlock SUPPLEMENTAL_PUNCTUATION;
        public static final UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_A;
        public static final UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_B;
        @Deprecated
        public static final UnicodeBlock SURROGATES_AREA = new UnicodeBlock("SURROGATES_AREA", false);
        public static final UnicodeBlock SYLOTI_NAGRI;
        public static final UnicodeBlock SYRIAC;
        public static final UnicodeBlock TAGALOG;
        public static final UnicodeBlock TAGBANWA;
        public static final UnicodeBlock TAGS;
        public static final UnicodeBlock TAI_LE;
        public static final UnicodeBlock TAI_THAM;
        public static final UnicodeBlock TAI_VIET;
        public static final UnicodeBlock TAI_XUAN_JING_SYMBOLS;
        public static final UnicodeBlock TAKRI;
        public static final UnicodeBlock TAMIL;
        public static final UnicodeBlock TELUGU;
        public static final UnicodeBlock THAANA;
        public static final UnicodeBlock THAI;
        public static final UnicodeBlock TIBETAN;
        public static final UnicodeBlock TIFINAGH;
        public static final UnicodeBlock TRANSPORT_AND_MAP_SYMBOLS;
        public static final UnicodeBlock UGARITIC;
        public static final UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS;
        public static final UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED;
        public static final UnicodeBlock VAI;
        public static final UnicodeBlock VARIATION_SELECTORS;
        public static final UnicodeBlock VARIATION_SELECTORS_SUPPLEMENT;
        public static final UnicodeBlock VEDIC_EXTENSIONS;
        public static final UnicodeBlock VERTICAL_FORMS;
        public static final UnicodeBlock YIJING_HEXAGRAM_SYMBOLS;
        public static final UnicodeBlock YI_RADICALS;
        public static final UnicodeBlock YI_SYLLABLES;
        private static final int[] blockStarts = {0, 128, 256, 384, 592, 688, 768, 880, 1024, NetworkStackConstants.IPV6_MIN_MTU, 1328, 1424, 1536, 1792, 1872, 1920, 1984, 2048, 2112, 2144, 2208, 2304, 2432, 2560, 2688, 2816, 2944, 3072, SafetyCenterEntry.ENTRY_SEVERITY_LEVEL_OK, 3328, 3456, 3584, 3712, 3840, 4096, 4256, 4352, 4608, 4992, 5024, 5120, 5760, 5792, 5888, 5920, 5952, 5984, 6016, 6144, 6320, 6400, 6480, 6528, 6624, 6656, 6688, 6832, 6912, 7040, 7104, 7168, 7248, 7296, 7360, 7376, 7424, 7552, 7616, 7680, 7936, 8192, 8304, 8352, 8400, 8448, 8528, 8592, 8704, 8960, 9216, 9280, 9312, 9472, 9600, 9632, 9728, 9984, 10176, 10224, 10240, 10496, 10624, 10752, 11008, 11264, 11360, 11392, 11520, 11568, 11648, 11744, 11776, 11904, 12032, 12256, 12272, 12288, 12352, 12448, 12544, 12592, 12688, 12704, 12736, 12784, 12800, 13056, 13312, 19904, 19968, 40960, 42128, 42192, 42240, 42560, 42656, 42752, 42784, 43008, 43056, 43072, 43136, 43232, 43264, 43312, 43360, 43392, 43488, 43520, 43616, 43648, 43744, 43776, 43824, 43968, 44032, 55216, 55296, 56192, 56320, 57344, 63744, 64256, 64336, 65024, 65040, 65056, 65072, 65104, 65136, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 65520, 65536, 65664, 65792, 65856, 65936, 66000, 66048, 66176, 66208, 66272, 66304, 66352, 66384, 66432, 66464, 66528, 66560, 66640, 66688, 66736, 67584, 67648, 67680, 67840, 67872, 67904, 67968, 68000, 68096, 68192, 68224, 68352, 68416, 68448, 68480, 68608, 68688, 69216, 69248, 69632, 69760, 69840, 69888, 69968, 70016, 70112, 71296, 71376, 73728, 74752, 74880, 77824, 78896, 92160, 92736, 93952, 94112, 110592, 110848, 118784, 119040, 119296, 119376, 119552, 119648, 119680, 119808, 120832, 126464, 126720, 126976, 127024, 127136, 127232, 127488, 127744, 128512, 128592, 128640, 128768, 128896, 131072, 173792, 173824, 177984, 178208, 194560, 195104, 917504, 917632, 917760, 918000, 983040, 1048576};
        private static final UnicodeBlock[] blocks;
        private static Map<String, UnicodeBlock> map = new HashMap(256);

        static {
            UnicodeBlock unicodeBlock = new UnicodeBlock("BASIC_LATIN", "BASIC LATIN", "BASICLATIN");
            BASIC_LATIN = unicodeBlock;
            UnicodeBlock unicodeBlock2 = new UnicodeBlock("LATIN_1_SUPPLEMENT", "LATIN-1 SUPPLEMENT", "LATIN-1SUPPLEMENT");
            LATIN_1_SUPPLEMENT = unicodeBlock2;
            UnicodeBlock unicodeBlock3 = new UnicodeBlock("LATIN_EXTENDED_A", "LATIN EXTENDED-A", "LATINEXTENDED-A");
            LATIN_EXTENDED_A = unicodeBlock3;
            UnicodeBlock unicodeBlock4 = new UnicodeBlock("LATIN_EXTENDED_B", "LATIN EXTENDED-B", "LATINEXTENDED-B");
            LATIN_EXTENDED_B = unicodeBlock4;
            UnicodeBlock unicodeBlock5 = new UnicodeBlock("IPA_EXTENSIONS", "IPA EXTENSIONS", "IPAEXTENSIONS");
            IPA_EXTENSIONS = unicodeBlock5;
            UnicodeBlock unicodeBlock6 = new UnicodeBlock("SPACING_MODIFIER_LETTERS", "SPACING MODIFIER LETTERS", "SPACINGMODIFIERLETTERS");
            SPACING_MODIFIER_LETTERS = unicodeBlock6;
            UnicodeBlock unicodeBlock7 = new UnicodeBlock("COMBINING_DIACRITICAL_MARKS", "COMBINING DIACRITICAL MARKS", "COMBININGDIACRITICALMARKS");
            COMBINING_DIACRITICAL_MARKS = unicodeBlock7;
            UnicodeBlock unicodeBlock8 = new UnicodeBlock("GREEK", "GREEK AND COPTIC", "GREEKANDCOPTIC");
            GREEK = unicodeBlock8;
            UnicodeBlock unicodeBlock9 = new UnicodeBlock("CYRILLIC");
            CYRILLIC = unicodeBlock9;
            UnicodeBlock unicodeBlock10 = new UnicodeBlock("ARMENIAN");
            ARMENIAN = unicodeBlock10;
            UnicodeBlock unicodeBlock11 = new UnicodeBlock("HEBREW");
            HEBREW = unicodeBlock11;
            UnicodeBlock unicodeBlock12 = new UnicodeBlock("ARABIC");
            ARABIC = unicodeBlock12;
            UnicodeBlock unicodeBlock13 = new UnicodeBlock("DEVANAGARI");
            DEVANAGARI = unicodeBlock13;
            UnicodeBlock unicodeBlock14 = new UnicodeBlock("BENGALI");
            BENGALI = unicodeBlock14;
            UnicodeBlock unicodeBlock15 = new UnicodeBlock("GURMUKHI");
            GURMUKHI = unicodeBlock15;
            UnicodeBlock unicodeBlock16 = unicodeBlock15;
            UnicodeBlock unicodeBlock17 = new UnicodeBlock("GUJARATI");
            GUJARATI = unicodeBlock17;
            UnicodeBlock unicodeBlock18 = unicodeBlock17;
            UnicodeBlock unicodeBlock19 = new UnicodeBlock("ORIYA");
            ORIYA = unicodeBlock19;
            UnicodeBlock unicodeBlock20 = unicodeBlock19;
            UnicodeBlock unicodeBlock21 = new UnicodeBlock("TAMIL");
            TAMIL = unicodeBlock21;
            UnicodeBlock unicodeBlock22 = unicodeBlock21;
            UnicodeBlock unicodeBlock23 = new UnicodeBlock("TELUGU");
            TELUGU = unicodeBlock23;
            UnicodeBlock unicodeBlock24 = unicodeBlock23;
            UnicodeBlock unicodeBlock25 = new UnicodeBlock("KANNADA");
            KANNADA = unicodeBlock25;
            UnicodeBlock unicodeBlock26 = unicodeBlock25;
            UnicodeBlock unicodeBlock27 = new UnicodeBlock("MALAYALAM");
            MALAYALAM = unicodeBlock27;
            UnicodeBlock unicodeBlock28 = unicodeBlock27;
            UnicodeBlock unicodeBlock29 = new UnicodeBlock("THAI");
            THAI = unicodeBlock29;
            UnicodeBlock unicodeBlock30 = unicodeBlock29;
            UnicodeBlock unicodeBlock31 = new UnicodeBlock("LAO");
            LAO = unicodeBlock31;
            UnicodeBlock unicodeBlock32 = unicodeBlock31;
            UnicodeBlock unicodeBlock33 = new UnicodeBlock("TIBETAN");
            TIBETAN = unicodeBlock33;
            UnicodeBlock unicodeBlock34 = unicodeBlock33;
            UnicodeBlock unicodeBlock35 = new UnicodeBlock("GEORGIAN");
            GEORGIAN = unicodeBlock35;
            UnicodeBlock unicodeBlock36 = unicodeBlock35;
            UnicodeBlock unicodeBlock37 = unicodeBlock14;
            UnicodeBlock unicodeBlock38 = new UnicodeBlock("HANGUL_JAMO", "HANGUL JAMO", "HANGULJAMO");
            HANGUL_JAMO = unicodeBlock38;
            UnicodeBlock unicodeBlock39 = unicodeBlock38;
            UnicodeBlock unicodeBlock40 = new UnicodeBlock("LATIN_EXTENDED_ADDITIONAL", "LATIN EXTENDED ADDITIONAL", "LATINEXTENDEDADDITIONAL");
            LATIN_EXTENDED_ADDITIONAL = unicodeBlock40;
            UnicodeBlock unicodeBlock41 = unicodeBlock40;
            UnicodeBlock unicodeBlock42 = new UnicodeBlock("GREEK_EXTENDED", "GREEK EXTENDED", "GREEKEXTENDED");
            GREEK_EXTENDED = unicodeBlock42;
            UnicodeBlock unicodeBlock43 = unicodeBlock42;
            UnicodeBlock unicodeBlock44 = new UnicodeBlock("GENERAL_PUNCTUATION", "GENERAL PUNCTUATION", "GENERALPUNCTUATION");
            GENERAL_PUNCTUATION = unicodeBlock44;
            UnicodeBlock unicodeBlock45 = unicodeBlock44;
            UnicodeBlock unicodeBlock46 = new UnicodeBlock("SUPERSCRIPTS_AND_SUBSCRIPTS", "SUPERSCRIPTS AND SUBSCRIPTS", "SUPERSCRIPTSANDSUBSCRIPTS");
            SUPERSCRIPTS_AND_SUBSCRIPTS = unicodeBlock46;
            UnicodeBlock unicodeBlock47 = unicodeBlock46;
            UnicodeBlock unicodeBlock48 = new UnicodeBlock("CURRENCY_SYMBOLS", "CURRENCY SYMBOLS", "CURRENCYSYMBOLS");
            CURRENCY_SYMBOLS = unicodeBlock48;
            UnicodeBlock unicodeBlock49 = unicodeBlock48;
            UnicodeBlock unicodeBlock50 = unicodeBlock13;
            UnicodeBlock unicodeBlock51 = unicodeBlock12;
            UnicodeBlock unicodeBlock52 = new UnicodeBlock("COMBINING_MARKS_FOR_SYMBOLS", "COMBINING DIACRITICAL MARKS FOR SYMBOLS", "COMBININGDIACRITICALMARKSFORSYMBOLS", "COMBINING MARKS FOR SYMBOLS", "COMBININGMARKSFORSYMBOLS");
            COMBINING_MARKS_FOR_SYMBOLS = unicodeBlock52;
            UnicodeBlock unicodeBlock53 = new UnicodeBlock("LETTERLIKE_SYMBOLS", "LETTERLIKE SYMBOLS", "LETTERLIKESYMBOLS");
            LETTERLIKE_SYMBOLS = unicodeBlock53;
            UnicodeBlock unicodeBlock54 = new UnicodeBlock("NUMBER_FORMS", "NUMBER FORMS", "NUMBERFORMS");
            NUMBER_FORMS = unicodeBlock54;
            UnicodeBlock unicodeBlock55 = new UnicodeBlock("ARROWS");
            ARROWS = unicodeBlock55;
            UnicodeBlock unicodeBlock56 = unicodeBlock55;
            UnicodeBlock unicodeBlock57 = unicodeBlock54;
            UnicodeBlock unicodeBlock58 = new UnicodeBlock("MATHEMATICAL_OPERATORS", "MATHEMATICAL OPERATORS", "MATHEMATICALOPERATORS");
            MATHEMATICAL_OPERATORS = unicodeBlock58;
            UnicodeBlock unicodeBlock59 = unicodeBlock58;
            UnicodeBlock unicodeBlock60 = new UnicodeBlock("MISCELLANEOUS_TECHNICAL", "MISCELLANEOUS TECHNICAL", "MISCELLANEOUSTECHNICAL");
            MISCELLANEOUS_TECHNICAL = unicodeBlock60;
            UnicodeBlock unicodeBlock61 = unicodeBlock60;
            UnicodeBlock unicodeBlock62 = new UnicodeBlock("CONTROL_PICTURES", "CONTROL PICTURES", "CONTROLPICTURES");
            CONTROL_PICTURES = unicodeBlock62;
            UnicodeBlock unicodeBlock63 = unicodeBlock62;
            UnicodeBlock unicodeBlock64 = new UnicodeBlock("OPTICAL_CHARACTER_RECOGNITION", "OPTICAL CHARACTER RECOGNITION", "OPTICALCHARACTERRECOGNITION");
            OPTICAL_CHARACTER_RECOGNITION = unicodeBlock64;
            UnicodeBlock unicodeBlock65 = unicodeBlock64;
            UnicodeBlock unicodeBlock66 = new UnicodeBlock("ENCLOSED_ALPHANUMERICS", "ENCLOSED ALPHANUMERICS", "ENCLOSEDALPHANUMERICS");
            ENCLOSED_ALPHANUMERICS = unicodeBlock66;
            UnicodeBlock unicodeBlock67 = unicodeBlock66;
            UnicodeBlock unicodeBlock68 = new UnicodeBlock("BOX_DRAWING", "BOX DRAWING", "BOXDRAWING");
            BOX_DRAWING = unicodeBlock68;
            UnicodeBlock unicodeBlock69 = unicodeBlock68;
            UnicodeBlock unicodeBlock70 = new UnicodeBlock("BLOCK_ELEMENTS", "BLOCK ELEMENTS", "BLOCKELEMENTS");
            BLOCK_ELEMENTS = unicodeBlock70;
            UnicodeBlock unicodeBlock71 = unicodeBlock70;
            UnicodeBlock unicodeBlock72 = new UnicodeBlock("GEOMETRIC_SHAPES", "GEOMETRIC SHAPES", "GEOMETRICSHAPES");
            GEOMETRIC_SHAPES = unicodeBlock72;
            UnicodeBlock unicodeBlock73 = unicodeBlock72;
            UnicodeBlock unicodeBlock74 = new UnicodeBlock("MISCELLANEOUS_SYMBOLS", "MISCELLANEOUS SYMBOLS", "MISCELLANEOUSSYMBOLS");
            MISCELLANEOUS_SYMBOLS = unicodeBlock74;
            UnicodeBlock unicodeBlock75 = new UnicodeBlock("DINGBATS");
            DINGBATS = unicodeBlock75;
            UnicodeBlock unicodeBlock76 = unicodeBlock75;
            UnicodeBlock unicodeBlock77 = unicodeBlock74;
            UnicodeBlock unicodeBlock78 = new UnicodeBlock("CJK_SYMBOLS_AND_PUNCTUATION", "CJK SYMBOLS AND PUNCTUATION", "CJKSYMBOLSANDPUNCTUATION");
            CJK_SYMBOLS_AND_PUNCTUATION = unicodeBlock78;
            UnicodeBlock unicodeBlock79 = new UnicodeBlock("HIRAGANA");
            HIRAGANA = unicodeBlock79;
            UnicodeBlock unicodeBlock80 = unicodeBlock79;
            UnicodeBlock unicodeBlock81 = new UnicodeBlock("KATAKANA");
            KATAKANA = unicodeBlock81;
            UnicodeBlock unicodeBlock82 = unicodeBlock81;
            UnicodeBlock unicodeBlock83 = new UnicodeBlock("BOPOMOFO");
            BOPOMOFO = unicodeBlock83;
            UnicodeBlock unicodeBlock84 = unicodeBlock83;
            UnicodeBlock unicodeBlock85 = unicodeBlock78;
            UnicodeBlock unicodeBlock86 = new UnicodeBlock("HANGUL_COMPATIBILITY_JAMO", "HANGUL COMPATIBILITY JAMO", "HANGULCOMPATIBILITYJAMO");
            HANGUL_COMPATIBILITY_JAMO = unicodeBlock86;
            UnicodeBlock unicodeBlock87 = new UnicodeBlock("KANBUN");
            KANBUN = unicodeBlock87;
            UnicodeBlock unicodeBlock88 = unicodeBlock87;
            UnicodeBlock unicodeBlock89 = unicodeBlock86;
            UnicodeBlock unicodeBlock90 = new UnicodeBlock("ENCLOSED_CJK_LETTERS_AND_MONTHS", "ENCLOSED CJK LETTERS AND MONTHS", "ENCLOSEDCJKLETTERSANDMONTHS");
            ENCLOSED_CJK_LETTERS_AND_MONTHS = unicodeBlock90;
            UnicodeBlock unicodeBlock91 = unicodeBlock90;
            UnicodeBlock unicodeBlock92 = new UnicodeBlock("CJK_COMPATIBILITY", "CJK COMPATIBILITY", "CJKCOMPATIBILITY");
            CJK_COMPATIBILITY = unicodeBlock92;
            UnicodeBlock unicodeBlock93 = unicodeBlock92;
            UnicodeBlock unicodeBlock94 = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS", "CJK UNIFIED IDEOGRAPHS", "CJKUNIFIEDIDEOGRAPHS");
            CJK_UNIFIED_IDEOGRAPHS = unicodeBlock94;
            UnicodeBlock unicodeBlock95 = unicodeBlock94;
            UnicodeBlock unicodeBlock96 = new UnicodeBlock("HANGUL_SYLLABLES", "HANGUL SYLLABLES", "HANGULSYLLABLES");
            HANGUL_SYLLABLES = unicodeBlock96;
            UnicodeBlock unicodeBlock97 = unicodeBlock96;
            UnicodeBlock unicodeBlock98 = new UnicodeBlock("PRIVATE_USE_AREA", "PRIVATE USE AREA", "PRIVATEUSEAREA");
            PRIVATE_USE_AREA = unicodeBlock98;
            UnicodeBlock unicodeBlock99 = unicodeBlock98;
            UnicodeBlock unicodeBlock100 = new UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS", "CJK COMPATIBILITY IDEOGRAPHS", "CJKCOMPATIBILITYIDEOGRAPHS");
            CJK_COMPATIBILITY_IDEOGRAPHS = unicodeBlock100;
            UnicodeBlock unicodeBlock101 = unicodeBlock100;
            UnicodeBlock unicodeBlock102 = new UnicodeBlock("ALPHABETIC_PRESENTATION_FORMS", "ALPHABETIC PRESENTATION FORMS", "ALPHABETICPRESENTATIONFORMS");
            ALPHABETIC_PRESENTATION_FORMS = unicodeBlock102;
            UnicodeBlock unicodeBlock103 = unicodeBlock102;
            UnicodeBlock unicodeBlock104 = new UnicodeBlock("ARABIC_PRESENTATION_FORMS_A", "ARABIC PRESENTATION FORMS-A", "ARABICPRESENTATIONFORMS-A");
            ARABIC_PRESENTATION_FORMS_A = unicodeBlock104;
            UnicodeBlock unicodeBlock105 = unicodeBlock104;
            UnicodeBlock unicodeBlock106 = new UnicodeBlock("COMBINING_HALF_MARKS", "COMBINING HALF MARKS", "COMBININGHALFMARKS");
            COMBINING_HALF_MARKS = unicodeBlock106;
            UnicodeBlock unicodeBlock107 = unicodeBlock106;
            UnicodeBlock unicodeBlock108 = new UnicodeBlock("CJK_COMPATIBILITY_FORMS", "CJK COMPATIBILITY FORMS", "CJKCOMPATIBILITYFORMS");
            CJK_COMPATIBILITY_FORMS = unicodeBlock108;
            UnicodeBlock unicodeBlock109 = unicodeBlock108;
            UnicodeBlock unicodeBlock110 = new UnicodeBlock("SMALL_FORM_VARIANTS", "SMALL FORM VARIANTS", "SMALLFORMVARIANTS");
            SMALL_FORM_VARIANTS = unicodeBlock110;
            UnicodeBlock unicodeBlock111 = unicodeBlock110;
            UnicodeBlock unicodeBlock112 = new UnicodeBlock("ARABIC_PRESENTATION_FORMS_B", "ARABIC PRESENTATION FORMS-B", "ARABICPRESENTATIONFORMS-B");
            ARABIC_PRESENTATION_FORMS_B = unicodeBlock112;
            UnicodeBlock unicodeBlock113 = unicodeBlock112;
            UnicodeBlock unicodeBlock114 = new UnicodeBlock("HALFWIDTH_AND_FULLWIDTH_FORMS", "HALFWIDTH AND FULLWIDTH FORMS", "HALFWIDTHANDFULLWIDTHFORMS");
            HALFWIDTH_AND_FULLWIDTH_FORMS = unicodeBlock114;
            UnicodeBlock unicodeBlock115 = new UnicodeBlock("SPECIALS");
            SPECIALS = unicodeBlock115;
            UnicodeBlock unicodeBlock116 = unicodeBlock115;
            UnicodeBlock unicodeBlock117 = unicodeBlock114;
            UnicodeBlock unicodeBlock118 = new UnicodeBlock("SYRIAC");
            SYRIAC = unicodeBlock118;
            UnicodeBlock unicodeBlock119 = new UnicodeBlock("THAANA");
            THAANA = unicodeBlock119;
            UnicodeBlock unicodeBlock120 = unicodeBlock53;
            UnicodeBlock unicodeBlock121 = new UnicodeBlock("SINHALA");
            SINHALA = unicodeBlock121;
            UnicodeBlock unicodeBlock122 = unicodeBlock52;
            UnicodeBlock unicodeBlock123 = new UnicodeBlock("MYANMAR");
            MYANMAR = unicodeBlock123;
            UnicodeBlock unicodeBlock124 = unicodeBlock123;
            UnicodeBlock unicodeBlock125 = new UnicodeBlock("ETHIOPIC");
            ETHIOPIC = unicodeBlock125;
            UnicodeBlock unicodeBlock126 = unicodeBlock125;
            UnicodeBlock unicodeBlock127 = new UnicodeBlock("CHEROKEE");
            CHEROKEE = unicodeBlock127;
            UnicodeBlock unicodeBlock128 = unicodeBlock127;
            UnicodeBlock unicodeBlock129 = unicodeBlock121;
            UnicodeBlock unicodeBlock130 = new UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS", "UNIFIED CANADIAN ABORIGINAL SYLLABICS", "UNIFIEDCANADIANABORIGINALSYLLABICS");
            UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS = unicodeBlock130;
            UnicodeBlock unicodeBlock131 = new UnicodeBlock("OGHAM");
            OGHAM = unicodeBlock131;
            UnicodeBlock unicodeBlock132 = unicodeBlock131;
            UnicodeBlock unicodeBlock133 = new UnicodeBlock("RUNIC");
            RUNIC = unicodeBlock133;
            UnicodeBlock unicodeBlock134 = unicodeBlock133;
            UnicodeBlock unicodeBlock135 = new UnicodeBlock("KHMER");
            KHMER = unicodeBlock135;
            UnicodeBlock unicodeBlock136 = unicodeBlock135;
            UnicodeBlock unicodeBlock137 = new UnicodeBlock("MONGOLIAN");
            MONGOLIAN = unicodeBlock137;
            UnicodeBlock unicodeBlock138 = unicodeBlock137;
            UnicodeBlock unicodeBlock139 = unicodeBlock130;
            UnicodeBlock unicodeBlock140 = new UnicodeBlock("BRAILLE_PATTERNS", "BRAILLE PATTERNS", "BRAILLEPATTERNS");
            BRAILLE_PATTERNS = unicodeBlock140;
            UnicodeBlock unicodeBlock141 = unicodeBlock140;
            UnicodeBlock unicodeBlock142 = new UnicodeBlock("CJK_RADICALS_SUPPLEMENT", "CJK RADICALS SUPPLEMENT", "CJKRADICALSSUPPLEMENT");
            CJK_RADICALS_SUPPLEMENT = unicodeBlock142;
            UnicodeBlock unicodeBlock143 = unicodeBlock142;
            UnicodeBlock unicodeBlock144 = new UnicodeBlock("KANGXI_RADICALS", "KANGXI RADICALS", "KANGXIRADICALS");
            KANGXI_RADICALS = unicodeBlock144;
            UnicodeBlock unicodeBlock145 = unicodeBlock144;
            UnicodeBlock unicodeBlock146 = new UnicodeBlock("IDEOGRAPHIC_DESCRIPTION_CHARACTERS", "IDEOGRAPHIC DESCRIPTION CHARACTERS", "IDEOGRAPHICDESCRIPTIONCHARACTERS");
            IDEOGRAPHIC_DESCRIPTION_CHARACTERS = unicodeBlock146;
            UnicodeBlock unicodeBlock147 = unicodeBlock146;
            UnicodeBlock unicodeBlock148 = new UnicodeBlock("BOPOMOFO_EXTENDED", "BOPOMOFO EXTENDED", "BOPOMOFOEXTENDED");
            BOPOMOFO_EXTENDED = unicodeBlock148;
            UnicodeBlock unicodeBlock149 = unicodeBlock148;
            UnicodeBlock unicodeBlock150 = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A", "CJK UNIFIED IDEOGRAPHS EXTENSION A", "CJKUNIFIEDIDEOGRAPHSEXTENSIONA");
            CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A = unicodeBlock150;
            UnicodeBlock unicodeBlock151 = unicodeBlock150;
            UnicodeBlock unicodeBlock152 = new UnicodeBlock("YI_SYLLABLES", "YI SYLLABLES", "YISYLLABLES");
            YI_SYLLABLES = unicodeBlock152;
            UnicodeBlock unicodeBlock153 = unicodeBlock152;
            UnicodeBlock unicodeBlock154 = new UnicodeBlock("YI_RADICALS", "YI RADICALS", "YIRADICALS");
            YI_RADICALS = unicodeBlock154;
            UnicodeBlock unicodeBlock155 = unicodeBlock154;
            UnicodeBlock unicodeBlock156 = unicodeBlock119;
            UnicodeBlock unicodeBlock157 = unicodeBlock118;
            UnicodeBlock unicodeBlock158 = new UnicodeBlock("CYRILLIC_SUPPLEMENTARY", "CYRILLIC SUPPLEMENTARY", "CYRILLICSUPPLEMENTARY", "CYRILLIC SUPPLEMENT", "CYRILLICSUPPLEMENT");
            CYRILLIC_SUPPLEMENTARY = unicodeBlock158;
            UnicodeBlock unicodeBlock159 = new UnicodeBlock("TAGALOG");
            TAGALOG = unicodeBlock159;
            UnicodeBlock unicodeBlock160 = new UnicodeBlock("HANUNOO");
            HANUNOO = unicodeBlock160;
            UnicodeBlock unicodeBlock161 = new UnicodeBlock("BUHID");
            BUHID = unicodeBlock161;
            UnicodeBlock unicodeBlock162 = unicodeBlock161;
            UnicodeBlock unicodeBlock163 = new UnicodeBlock("TAGBANWA");
            TAGBANWA = unicodeBlock163;
            UnicodeBlock unicodeBlock164 = unicodeBlock163;
            UnicodeBlock unicodeBlock165 = new UnicodeBlock("LIMBU");
            LIMBU = unicodeBlock165;
            UnicodeBlock unicodeBlock166 = unicodeBlock165;
            UnicodeBlock unicodeBlock167 = unicodeBlock160;
            UnicodeBlock unicodeBlock168 = new UnicodeBlock("TAI_LE", "TAI LE", "TAILE");
            TAI_LE = unicodeBlock168;
            UnicodeBlock unicodeBlock169 = unicodeBlock168;
            UnicodeBlock unicodeBlock170 = new UnicodeBlock("KHMER_SYMBOLS", "KHMER SYMBOLS", "KHMERSYMBOLS");
            KHMER_SYMBOLS = unicodeBlock170;
            UnicodeBlock unicodeBlock171 = unicodeBlock170;
            UnicodeBlock unicodeBlock172 = new UnicodeBlock("PHONETIC_EXTENSIONS", "PHONETIC EXTENSIONS", "PHONETICEXTENSIONS");
            PHONETIC_EXTENSIONS = unicodeBlock172;
            UnicodeBlock unicodeBlock173 = unicodeBlock172;
            UnicodeBlock unicodeBlock174 = new UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A", "MISCELLANEOUS MATHEMATICAL SYMBOLS-A", "MISCELLANEOUSMATHEMATICALSYMBOLS-A");
            MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A = unicodeBlock174;
            UnicodeBlock unicodeBlock175 = unicodeBlock174;
            UnicodeBlock unicodeBlock176 = new UnicodeBlock("SUPPLEMENTAL_ARROWS_A", "SUPPLEMENTAL ARROWS-A", "SUPPLEMENTALARROWS-A");
            SUPPLEMENTAL_ARROWS_A = unicodeBlock176;
            UnicodeBlock unicodeBlock177 = unicodeBlock176;
            UnicodeBlock unicodeBlock178 = new UnicodeBlock("SUPPLEMENTAL_ARROWS_B", "SUPPLEMENTAL ARROWS-B", "SUPPLEMENTALARROWS-B");
            SUPPLEMENTAL_ARROWS_B = unicodeBlock178;
            UnicodeBlock unicodeBlock179 = unicodeBlock178;
            UnicodeBlock unicodeBlock180 = new UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B", "MISCELLANEOUS MATHEMATICAL SYMBOLS-B", "MISCELLANEOUSMATHEMATICALSYMBOLS-B");
            MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B = unicodeBlock180;
            UnicodeBlock unicodeBlock181 = unicodeBlock180;
            UnicodeBlock unicodeBlock182 = new UnicodeBlock("SUPPLEMENTAL_MATHEMATICAL_OPERATORS", "SUPPLEMENTAL MATHEMATICAL OPERATORS", "SUPPLEMENTALMATHEMATICALOPERATORS");
            SUPPLEMENTAL_MATHEMATICAL_OPERATORS = unicodeBlock182;
            UnicodeBlock unicodeBlock183 = unicodeBlock182;
            UnicodeBlock unicodeBlock184 = new UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_ARROWS", "MISCELLANEOUS SYMBOLS AND ARROWS", "MISCELLANEOUSSYMBOLSANDARROWS");
            MISCELLANEOUS_SYMBOLS_AND_ARROWS = unicodeBlock184;
            UnicodeBlock unicodeBlock185 = unicodeBlock184;
            UnicodeBlock unicodeBlock186 = new UnicodeBlock("KATAKANA_PHONETIC_EXTENSIONS", "KATAKANA PHONETIC EXTENSIONS", "KATAKANAPHONETICEXTENSIONS");
            KATAKANA_PHONETIC_EXTENSIONS = unicodeBlock186;
            UnicodeBlock unicodeBlock187 = unicodeBlock186;
            UnicodeBlock unicodeBlock188 = new UnicodeBlock("YIJING_HEXAGRAM_SYMBOLS", "YIJING HEXAGRAM SYMBOLS", "YIJINGHEXAGRAMSYMBOLS");
            YIJING_HEXAGRAM_SYMBOLS = unicodeBlock188;
            UnicodeBlock unicodeBlock189 = unicodeBlock188;
            UnicodeBlock unicodeBlock190 = new UnicodeBlock("VARIATION_SELECTORS", "VARIATION SELECTORS", "VARIATIONSELECTORS");
            VARIATION_SELECTORS = unicodeBlock190;
            UnicodeBlock unicodeBlock191 = unicodeBlock190;
            UnicodeBlock unicodeBlock192 = new UnicodeBlock("LINEAR_B_SYLLABARY", "LINEAR B SYLLABARY", "LINEARBSYLLABARY");
            LINEAR_B_SYLLABARY = unicodeBlock192;
            UnicodeBlock unicodeBlock193 = unicodeBlock192;
            UnicodeBlock unicodeBlock194 = new UnicodeBlock("LINEAR_B_IDEOGRAMS", "LINEAR B IDEOGRAMS", "LINEARBIDEOGRAMS");
            LINEAR_B_IDEOGRAMS = unicodeBlock194;
            UnicodeBlock unicodeBlock195 = unicodeBlock194;
            UnicodeBlock unicodeBlock196 = new UnicodeBlock("AEGEAN_NUMBERS", "AEGEAN NUMBERS", "AEGEANNUMBERS");
            AEGEAN_NUMBERS = unicodeBlock196;
            UnicodeBlock unicodeBlock197 = unicodeBlock196;
            UnicodeBlock unicodeBlock198 = new UnicodeBlock("OLD_ITALIC", "OLD ITALIC", "OLDITALIC");
            OLD_ITALIC = unicodeBlock198;
            UnicodeBlock unicodeBlock199 = new UnicodeBlock("GOTHIC");
            GOTHIC = unicodeBlock199;
            UnicodeBlock unicodeBlock200 = unicodeBlock199;
            UnicodeBlock unicodeBlock201 = new UnicodeBlock("UGARITIC");
            UGARITIC = unicodeBlock201;
            UnicodeBlock unicodeBlock202 = unicodeBlock201;
            UnicodeBlock unicodeBlock203 = new UnicodeBlock("DESERET");
            DESERET = unicodeBlock203;
            UnicodeBlock unicodeBlock204 = unicodeBlock203;
            UnicodeBlock unicodeBlock205 = new UnicodeBlock("SHAVIAN");
            SHAVIAN = unicodeBlock205;
            UnicodeBlock unicodeBlock206 = unicodeBlock205;
            UnicodeBlock unicodeBlock207 = new UnicodeBlock("OSMANYA");
            OSMANYA = unicodeBlock207;
            UnicodeBlock unicodeBlock208 = unicodeBlock207;
            UnicodeBlock unicodeBlock209 = unicodeBlock198;
            UnicodeBlock unicodeBlock210 = new UnicodeBlock("CYPRIOT_SYLLABARY", "CYPRIOT SYLLABARY", "CYPRIOTSYLLABARY");
            CYPRIOT_SYLLABARY = unicodeBlock210;
            UnicodeBlock unicodeBlock211 = unicodeBlock210;
            UnicodeBlock unicodeBlock212 = new UnicodeBlock("BYZANTINE_MUSICAL_SYMBOLS", "BYZANTINE MUSICAL SYMBOLS", "BYZANTINEMUSICALSYMBOLS");
            BYZANTINE_MUSICAL_SYMBOLS = unicodeBlock212;
            UnicodeBlock unicodeBlock213 = unicodeBlock212;
            UnicodeBlock unicodeBlock214 = new UnicodeBlock("MUSICAL_SYMBOLS", "MUSICAL SYMBOLS", "MUSICALSYMBOLS");
            MUSICAL_SYMBOLS = unicodeBlock214;
            UnicodeBlock unicodeBlock215 = unicodeBlock214;
            UnicodeBlock unicodeBlock216 = new UnicodeBlock("TAI_XUAN_JING_SYMBOLS", "TAI XUAN JING SYMBOLS", "TAIXUANJINGSYMBOLS");
            TAI_XUAN_JING_SYMBOLS = unicodeBlock216;
            UnicodeBlock unicodeBlock217 = unicodeBlock216;
            UnicodeBlock unicodeBlock218 = new UnicodeBlock("MATHEMATICAL_ALPHANUMERIC_SYMBOLS", "MATHEMATICAL ALPHANUMERIC SYMBOLS", "MATHEMATICALALPHANUMERICSYMBOLS");
            MATHEMATICAL_ALPHANUMERIC_SYMBOLS = unicodeBlock218;
            UnicodeBlock unicodeBlock219 = unicodeBlock218;
            UnicodeBlock unicodeBlock220 = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B", "CJK UNIFIED IDEOGRAPHS EXTENSION B", "CJKUNIFIEDIDEOGRAPHSEXTENSIONB");
            CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B = unicodeBlock220;
            UnicodeBlock unicodeBlock221 = unicodeBlock220;
            UnicodeBlock unicodeBlock222 = new UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT", "CJK COMPATIBILITY IDEOGRAPHS SUPPLEMENT", "CJKCOMPATIBILITYIDEOGRAPHSSUPPLEMENT");
            CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT = unicodeBlock222;
            UnicodeBlock unicodeBlock223 = new UnicodeBlock("TAGS");
            TAGS = unicodeBlock223;
            UnicodeBlock unicodeBlock224 = unicodeBlock223;
            UnicodeBlock unicodeBlock225 = unicodeBlock222;
            UnicodeBlock unicodeBlock226 = new UnicodeBlock("VARIATION_SELECTORS_SUPPLEMENT", "VARIATION SELECTORS SUPPLEMENT", "VARIATIONSELECTORSSUPPLEMENT");
            VARIATION_SELECTORS_SUPPLEMENT = unicodeBlock226;
            UnicodeBlock unicodeBlock227 = unicodeBlock226;
            UnicodeBlock unicodeBlock228 = new UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_A", "SUPPLEMENTARY PRIVATE USE AREA-A", "SUPPLEMENTARYPRIVATEUSEAREA-A");
            SUPPLEMENTARY_PRIVATE_USE_AREA_A = unicodeBlock228;
            UnicodeBlock unicodeBlock229 = unicodeBlock228;
            UnicodeBlock unicodeBlock230 = new UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_B", "SUPPLEMENTARY PRIVATE USE AREA-B", "SUPPLEMENTARYPRIVATEUSEAREA-B");
            SUPPLEMENTARY_PRIVATE_USE_AREA_B = unicodeBlock230;
            UnicodeBlock unicodeBlock231 = unicodeBlock230;
            UnicodeBlock unicodeBlock232 = new UnicodeBlock("HIGH_SURROGATES", "HIGH SURROGATES", "HIGHSURROGATES");
            HIGH_SURROGATES = unicodeBlock232;
            UnicodeBlock unicodeBlock233 = unicodeBlock232;
            UnicodeBlock unicodeBlock234 = new UnicodeBlock("HIGH_PRIVATE_USE_SURROGATES", "HIGH PRIVATE USE SURROGATES", "HIGHPRIVATEUSESURROGATES");
            HIGH_PRIVATE_USE_SURROGATES = unicodeBlock234;
            UnicodeBlock unicodeBlock235 = unicodeBlock234;
            UnicodeBlock unicodeBlock236 = new UnicodeBlock("LOW_SURROGATES", "LOW SURROGATES", "LOWSURROGATES");
            LOW_SURROGATES = unicodeBlock236;
            UnicodeBlock unicodeBlock237 = unicodeBlock236;
            UnicodeBlock unicodeBlock238 = new UnicodeBlock("ARABIC_SUPPLEMENT", "ARABIC SUPPLEMENT", "ARABICSUPPLEMENT");
            ARABIC_SUPPLEMENT = unicodeBlock238;
            UnicodeBlock unicodeBlock239 = new UnicodeBlock("NKO");
            NKO = unicodeBlock239;
            UnicodeBlock unicodeBlock240 = unicodeBlock159;
            UnicodeBlock unicodeBlock241 = new UnicodeBlock("SAMARITAN");
            SAMARITAN = unicodeBlock241;
            UnicodeBlock unicodeBlock242 = unicodeBlock241;
            UnicodeBlock unicodeBlock243 = new UnicodeBlock("MANDAIC");
            MANDAIC = unicodeBlock243;
            UnicodeBlock unicodeBlock244 = unicodeBlock243;
            UnicodeBlock unicodeBlock245 = unicodeBlock239;
            UnicodeBlock unicodeBlock246 = new UnicodeBlock("ETHIOPIC_SUPPLEMENT", "ETHIOPIC SUPPLEMENT", "ETHIOPICSUPPLEMENT");
            ETHIOPIC_SUPPLEMENT = unicodeBlock246;
            UnicodeBlock unicodeBlock247 = unicodeBlock246;
            UnicodeBlock unicodeBlock248 = new UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED", "UNIFIED CANADIAN ABORIGINAL SYLLABICS EXTENDED", "UNIFIEDCANADIANABORIGINALSYLLABICSEXTENDED");
            UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED = unicodeBlock248;
            UnicodeBlock unicodeBlock249 = unicodeBlock248;
            UnicodeBlock unicodeBlock250 = new UnicodeBlock("NEW_TAI_LUE", "NEW TAI LUE", "NEWTAILUE");
            NEW_TAI_LUE = unicodeBlock250;
            UnicodeBlock unicodeBlock251 = new UnicodeBlock("BUGINESE");
            BUGINESE = unicodeBlock251;
            UnicodeBlock unicodeBlock252 = unicodeBlock251;
            UnicodeBlock unicodeBlock253 = unicodeBlock250;
            UnicodeBlock unicodeBlock254 = new UnicodeBlock("TAI_THAM", "TAI THAM", "TAITHAM");
            TAI_THAM = unicodeBlock254;
            UnicodeBlock unicodeBlock255 = new UnicodeBlock("BALINESE");
            BALINESE = unicodeBlock255;
            UnicodeBlock unicodeBlock256 = unicodeBlock255;
            UnicodeBlock unicodeBlock257 = new UnicodeBlock("SUNDANESE");
            SUNDANESE = unicodeBlock257;
            UnicodeBlock unicodeBlock258 = unicodeBlock257;
            UnicodeBlock unicodeBlock259 = new UnicodeBlock("BATAK");
            BATAK = unicodeBlock259;
            UnicodeBlock unicodeBlock260 = unicodeBlock259;
            UnicodeBlock unicodeBlock261 = new UnicodeBlock("LEPCHA");
            LEPCHA = unicodeBlock261;
            UnicodeBlock unicodeBlock262 = unicodeBlock261;
            UnicodeBlock unicodeBlock263 = unicodeBlock254;
            UnicodeBlock unicodeBlock264 = new UnicodeBlock("OL_CHIKI", "OL CHIKI", "OLCHIKI");
            OL_CHIKI = unicodeBlock264;
            UnicodeBlock unicodeBlock265 = unicodeBlock264;
            UnicodeBlock unicodeBlock266 = new UnicodeBlock("VEDIC_EXTENSIONS", "VEDIC EXTENSIONS", "VEDICEXTENSIONS");
            VEDIC_EXTENSIONS = unicodeBlock266;
            UnicodeBlock unicodeBlock267 = unicodeBlock266;
            UnicodeBlock unicodeBlock268 = new UnicodeBlock("PHONETIC_EXTENSIONS_SUPPLEMENT", "PHONETIC EXTENSIONS SUPPLEMENT", "PHONETICEXTENSIONSSUPPLEMENT");
            PHONETIC_EXTENSIONS_SUPPLEMENT = unicodeBlock268;
            UnicodeBlock unicodeBlock269 = unicodeBlock268;
            UnicodeBlock unicodeBlock270 = new UnicodeBlock("COMBINING_DIACRITICAL_MARKS_SUPPLEMENT", "COMBINING DIACRITICAL MARKS SUPPLEMENT", "COMBININGDIACRITICALMARKSSUPPLEMENT");
            COMBINING_DIACRITICAL_MARKS_SUPPLEMENT = unicodeBlock270;
            UnicodeBlock unicodeBlock271 = new UnicodeBlock("GLAGOLITIC");
            GLAGOLITIC = unicodeBlock271;
            UnicodeBlock unicodeBlock272 = unicodeBlock271;
            UnicodeBlock unicodeBlock273 = unicodeBlock270;
            UnicodeBlock unicodeBlock274 = new UnicodeBlock("LATIN_EXTENDED_C", "LATIN EXTENDED-C", "LATINEXTENDED-C");
            LATIN_EXTENDED_C = unicodeBlock274;
            UnicodeBlock unicodeBlock275 = new UnicodeBlock("COPTIC");
            COPTIC = unicodeBlock275;
            UnicodeBlock unicodeBlock276 = unicodeBlock275;
            UnicodeBlock unicodeBlock277 = unicodeBlock274;
            UnicodeBlock unicodeBlock278 = new UnicodeBlock("GEORGIAN_SUPPLEMENT", "GEORGIAN SUPPLEMENT", "GEORGIANSUPPLEMENT");
            GEORGIAN_SUPPLEMENT = unicodeBlock278;
            UnicodeBlock unicodeBlock279 = new UnicodeBlock("TIFINAGH");
            TIFINAGH = unicodeBlock279;
            UnicodeBlock unicodeBlock280 = unicodeBlock279;
            UnicodeBlock unicodeBlock281 = unicodeBlock278;
            UnicodeBlock unicodeBlock282 = new UnicodeBlock("ETHIOPIC_EXTENDED", "ETHIOPIC EXTENDED", "ETHIOPICEXTENDED");
            ETHIOPIC_EXTENDED = unicodeBlock282;
            UnicodeBlock unicodeBlock283 = unicodeBlock282;
            UnicodeBlock unicodeBlock284 = new UnicodeBlock("CYRILLIC_EXTENDED_A", "CYRILLIC EXTENDED-A", "CYRILLICEXTENDED-A");
            CYRILLIC_EXTENDED_A = unicodeBlock284;
            UnicodeBlock unicodeBlock285 = unicodeBlock284;
            UnicodeBlock unicodeBlock286 = new UnicodeBlock("SUPPLEMENTAL_PUNCTUATION", "SUPPLEMENTAL PUNCTUATION", "SUPPLEMENTALPUNCTUATION");
            SUPPLEMENTAL_PUNCTUATION = unicodeBlock286;
            UnicodeBlock unicodeBlock287 = unicodeBlock286;
            UnicodeBlock unicodeBlock288 = new UnicodeBlock("CJK_STROKES", "CJK STROKES", "CJKSTROKES");
            CJK_STROKES = unicodeBlock288;
            UnicodeBlock unicodeBlock289 = new UnicodeBlock("LISU");
            LISU = unicodeBlock289;
            UnicodeBlock unicodeBlock290 = unicodeBlock289;
            UnicodeBlock unicodeBlock291 = new UnicodeBlock("VAI");
            VAI = unicodeBlock291;
            UnicodeBlock unicodeBlock292 = unicodeBlock291;
            UnicodeBlock unicodeBlock293 = unicodeBlock288;
            UnicodeBlock unicodeBlock294 = new UnicodeBlock("CYRILLIC_EXTENDED_B", "CYRILLIC EXTENDED-B", "CYRILLICEXTENDED-B");
            CYRILLIC_EXTENDED_B = unicodeBlock294;
            UnicodeBlock unicodeBlock295 = new UnicodeBlock("BAMUM");
            BAMUM = unicodeBlock295;
            UnicodeBlock unicodeBlock296 = unicodeBlock295;
            UnicodeBlock unicodeBlock297 = unicodeBlock294;
            UnicodeBlock unicodeBlock298 = new UnicodeBlock("MODIFIER_TONE_LETTERS", "MODIFIER TONE LETTERS", "MODIFIERTONELETTERS");
            MODIFIER_TONE_LETTERS = unicodeBlock298;
            UnicodeBlock unicodeBlock299 = unicodeBlock298;
            UnicodeBlock unicodeBlock300 = new UnicodeBlock("LATIN_EXTENDED_D", "LATIN EXTENDED-D", "LATINEXTENDED-D");
            LATIN_EXTENDED_D = unicodeBlock300;
            UnicodeBlock unicodeBlock301 = unicodeBlock300;
            UnicodeBlock unicodeBlock302 = new UnicodeBlock("SYLOTI_NAGRI", "SYLOTI NAGRI", "SYLOTINAGRI");
            SYLOTI_NAGRI = unicodeBlock302;
            UnicodeBlock unicodeBlock303 = unicodeBlock302;
            UnicodeBlock unicodeBlock304 = new UnicodeBlock("COMMON_INDIC_NUMBER_FORMS", "COMMON INDIC NUMBER FORMS", "COMMONINDICNUMBERFORMS");
            COMMON_INDIC_NUMBER_FORMS = unicodeBlock304;
            UnicodeBlock unicodeBlock305 = unicodeBlock304;
            UnicodeBlock unicodeBlock306 = new UnicodeBlock("PHAGS_PA", "PHAGS-PA");
            PHAGS_PA = unicodeBlock306;
            UnicodeBlock unicodeBlock307 = new UnicodeBlock("SAURASHTRA");
            SAURASHTRA = unicodeBlock307;
            UnicodeBlock unicodeBlock308 = unicodeBlock307;
            UnicodeBlock unicodeBlock309 = unicodeBlock306;
            UnicodeBlock unicodeBlock310 = new UnicodeBlock("DEVANAGARI_EXTENDED", "DEVANAGARI EXTENDED", "DEVANAGARIEXTENDED");
            DEVANAGARI_EXTENDED = unicodeBlock310;
            UnicodeBlock unicodeBlock311 = unicodeBlock310;
            UnicodeBlock unicodeBlock312 = new UnicodeBlock("KAYAH_LI", "KAYAH LI", "KAYAHLI");
            KAYAH_LI = unicodeBlock312;
            UnicodeBlock unicodeBlock313 = new UnicodeBlock("REJANG");
            REJANG = unicodeBlock313;
            UnicodeBlock unicodeBlock314 = unicodeBlock313;
            UnicodeBlock unicodeBlock315 = unicodeBlock312;
            UnicodeBlock unicodeBlock316 = new UnicodeBlock("HANGUL_JAMO_EXTENDED_A", "HANGUL JAMO EXTENDED-A", "HANGULJAMOEXTENDED-A");
            HANGUL_JAMO_EXTENDED_A = unicodeBlock316;
            UnicodeBlock unicodeBlock317 = new UnicodeBlock("JAVANESE");
            JAVANESE = unicodeBlock317;
            UnicodeBlock unicodeBlock318 = unicodeBlock317;
            UnicodeBlock unicodeBlock319 = new UnicodeBlock("CHAM");
            CHAM = unicodeBlock319;
            UnicodeBlock unicodeBlock320 = unicodeBlock319;
            UnicodeBlock unicodeBlock321 = unicodeBlock316;
            UnicodeBlock unicodeBlock322 = new UnicodeBlock("MYANMAR_EXTENDED_A", "MYANMAR EXTENDED-A", "MYANMAREXTENDED-A");
            MYANMAR_EXTENDED_A = unicodeBlock322;
            UnicodeBlock unicodeBlock323 = unicodeBlock322;
            UnicodeBlock unicodeBlock324 = new UnicodeBlock("TAI_VIET", "TAI VIET", "TAIVIET");
            TAI_VIET = unicodeBlock324;
            UnicodeBlock unicodeBlock325 = unicodeBlock324;
            UnicodeBlock unicodeBlock326 = new UnicodeBlock("ETHIOPIC_EXTENDED_A", "ETHIOPIC EXTENDED-A", "ETHIOPICEXTENDED-A");
            ETHIOPIC_EXTENDED_A = unicodeBlock326;
            UnicodeBlock unicodeBlock327 = unicodeBlock326;
            UnicodeBlock unicodeBlock328 = new UnicodeBlock("MEETEI_MAYEK", "MEETEI MAYEK", "MEETEIMAYEK");
            MEETEI_MAYEK = unicodeBlock328;
            UnicodeBlock unicodeBlock329 = unicodeBlock328;
            UnicodeBlock unicodeBlock330 = new UnicodeBlock("HANGUL_JAMO_EXTENDED_B", "HANGUL JAMO EXTENDED-B", "HANGULJAMOEXTENDED-B");
            HANGUL_JAMO_EXTENDED_B = unicodeBlock330;
            UnicodeBlock unicodeBlock331 = unicodeBlock330;
            UnicodeBlock unicodeBlock332 = new UnicodeBlock("VERTICAL_FORMS", "VERTICAL FORMS", "VERTICALFORMS");
            VERTICAL_FORMS = unicodeBlock332;
            UnicodeBlock unicodeBlock333 = unicodeBlock332;
            UnicodeBlock unicodeBlock334 = new UnicodeBlock("ANCIENT_GREEK_NUMBERS", "ANCIENT GREEK NUMBERS", "ANCIENTGREEKNUMBERS");
            ANCIENT_GREEK_NUMBERS = unicodeBlock334;
            UnicodeBlock unicodeBlock335 = unicodeBlock334;
            UnicodeBlock unicodeBlock336 = new UnicodeBlock("ANCIENT_SYMBOLS", "ANCIENT SYMBOLS", "ANCIENTSYMBOLS");
            ANCIENT_SYMBOLS = unicodeBlock336;
            UnicodeBlock unicodeBlock337 = unicodeBlock336;
            UnicodeBlock unicodeBlock338 = new UnicodeBlock("PHAISTOS_DISC", "PHAISTOS DISC", "PHAISTOSDISC");
            PHAISTOS_DISC = unicodeBlock338;
            UnicodeBlock unicodeBlock339 = new UnicodeBlock("LYCIAN");
            LYCIAN = unicodeBlock339;
            UnicodeBlock unicodeBlock340 = unicodeBlock339;
            UnicodeBlock unicodeBlock341 = new UnicodeBlock("CARIAN");
            CARIAN = unicodeBlock341;
            UnicodeBlock unicodeBlock342 = unicodeBlock341;
            UnicodeBlock unicodeBlock343 = unicodeBlock338;
            UnicodeBlock unicodeBlock344 = new UnicodeBlock("OLD_PERSIAN", "OLD PERSIAN", "OLDPERSIAN");
            OLD_PERSIAN = unicodeBlock344;
            UnicodeBlock unicodeBlock345 = unicodeBlock344;
            UnicodeBlock unicodeBlock346 = new UnicodeBlock("IMPERIAL_ARAMAIC", "IMPERIAL ARAMAIC", "IMPERIALARAMAIC");
            IMPERIAL_ARAMAIC = unicodeBlock346;
            UnicodeBlock unicodeBlock347 = new UnicodeBlock("PHOENICIAN");
            PHOENICIAN = unicodeBlock347;
            UnicodeBlock unicodeBlock348 = unicodeBlock347;
            UnicodeBlock unicodeBlock349 = new UnicodeBlock("LYDIAN");
            LYDIAN = unicodeBlock349;
            UnicodeBlock unicodeBlock350 = unicodeBlock349;
            UnicodeBlock unicodeBlock351 = new UnicodeBlock("KHAROSHTHI");
            KHAROSHTHI = unicodeBlock351;
            UnicodeBlock unicodeBlock352 = unicodeBlock351;
            UnicodeBlock unicodeBlock353 = unicodeBlock346;
            UnicodeBlock unicodeBlock354 = new UnicodeBlock("OLD_SOUTH_ARABIAN", "OLD SOUTH ARABIAN", "OLDSOUTHARABIAN");
            OLD_SOUTH_ARABIAN = unicodeBlock354;
            UnicodeBlock unicodeBlock355 = new UnicodeBlock("AVESTAN");
            AVESTAN = unicodeBlock355;
            UnicodeBlock unicodeBlock356 = unicodeBlock355;
            UnicodeBlock unicodeBlock357 = unicodeBlock354;
            UnicodeBlock unicodeBlock358 = new UnicodeBlock("INSCRIPTIONAL_PARTHIAN", "INSCRIPTIONAL PARTHIAN", "INSCRIPTIONALPARTHIAN");
            INSCRIPTIONAL_PARTHIAN = unicodeBlock358;
            UnicodeBlock unicodeBlock359 = unicodeBlock358;
            UnicodeBlock unicodeBlock360 = new UnicodeBlock("INSCRIPTIONAL_PAHLAVI", "INSCRIPTIONAL PAHLAVI", "INSCRIPTIONALPAHLAVI");
            INSCRIPTIONAL_PAHLAVI = unicodeBlock360;
            UnicodeBlock unicodeBlock361 = unicodeBlock360;
            UnicodeBlock unicodeBlock362 = new UnicodeBlock("OLD_TURKIC", "OLD TURKIC", "OLDTURKIC");
            OLD_TURKIC = unicodeBlock362;
            UnicodeBlock unicodeBlock363 = unicodeBlock362;
            UnicodeBlock unicodeBlock364 = new UnicodeBlock("RUMI_NUMERAL_SYMBOLS", "RUMI NUMERAL SYMBOLS", "RUMINUMERALSYMBOLS");
            RUMI_NUMERAL_SYMBOLS = unicodeBlock364;
            UnicodeBlock unicodeBlock365 = new UnicodeBlock("BRAHMI");
            BRAHMI = unicodeBlock365;
            UnicodeBlock unicodeBlock366 = unicodeBlock365;
            UnicodeBlock unicodeBlock367 = new UnicodeBlock("KAITHI");
            KAITHI = unicodeBlock367;
            UnicodeBlock unicodeBlock368 = unicodeBlock367;
            UnicodeBlock unicodeBlock369 = new UnicodeBlock("CUNEIFORM");
            CUNEIFORM = unicodeBlock369;
            UnicodeBlock unicodeBlock370 = unicodeBlock369;
            UnicodeBlock unicodeBlock371 = unicodeBlock364;
            UnicodeBlock unicodeBlock372 = new UnicodeBlock("CUNEIFORM_NUMBERS_AND_PUNCTUATION", "CUNEIFORM NUMBERS AND PUNCTUATION", "CUNEIFORMNUMBERSANDPUNCTUATION");
            CUNEIFORM_NUMBERS_AND_PUNCTUATION = unicodeBlock372;
            UnicodeBlock unicodeBlock373 = unicodeBlock372;
            UnicodeBlock unicodeBlock374 = new UnicodeBlock("EGYPTIAN_HIEROGLYPHS", "EGYPTIAN HIEROGLYPHS", "EGYPTIANHIEROGLYPHS");
            EGYPTIAN_HIEROGLYPHS = unicodeBlock374;
            UnicodeBlock unicodeBlock375 = unicodeBlock374;
            UnicodeBlock unicodeBlock376 = new UnicodeBlock("BAMUM_SUPPLEMENT", "BAMUM SUPPLEMENT", "BAMUMSUPPLEMENT");
            BAMUM_SUPPLEMENT = unicodeBlock376;
            UnicodeBlock unicodeBlock377 = unicodeBlock376;
            UnicodeBlock unicodeBlock378 = new UnicodeBlock("KANA_SUPPLEMENT", "KANA SUPPLEMENT", "KANASUPPLEMENT");
            KANA_SUPPLEMENT = unicodeBlock378;
            UnicodeBlock unicodeBlock379 = unicodeBlock378;
            UnicodeBlock unicodeBlock380 = new UnicodeBlock("ANCIENT_GREEK_MUSICAL_NOTATION", "ANCIENT GREEK MUSICAL NOTATION", "ANCIENTGREEKMUSICALNOTATION");
            ANCIENT_GREEK_MUSICAL_NOTATION = unicodeBlock380;
            UnicodeBlock unicodeBlock381 = unicodeBlock380;
            UnicodeBlock unicodeBlock382 = new UnicodeBlock("COUNTING_ROD_NUMERALS", "COUNTING ROD NUMERALS", "COUNTINGRODNUMERALS");
            COUNTING_ROD_NUMERALS = unicodeBlock382;
            UnicodeBlock unicodeBlock383 = unicodeBlock382;
            UnicodeBlock unicodeBlock384 = new UnicodeBlock("MAHJONG_TILES", "MAHJONG TILES", "MAHJONGTILES");
            MAHJONG_TILES = unicodeBlock384;
            UnicodeBlock unicodeBlock385 = unicodeBlock384;
            UnicodeBlock unicodeBlock386 = new UnicodeBlock("DOMINO_TILES", "DOMINO TILES", "DOMINOTILES");
            DOMINO_TILES = unicodeBlock386;
            UnicodeBlock unicodeBlock387 = unicodeBlock386;
            UnicodeBlock unicodeBlock388 = new UnicodeBlock("PLAYING_CARDS", "PLAYING CARDS", "PLAYINGCARDS");
            PLAYING_CARDS = unicodeBlock388;
            UnicodeBlock unicodeBlock389 = unicodeBlock388;
            UnicodeBlock unicodeBlock390 = new UnicodeBlock("ENCLOSED_ALPHANUMERIC_SUPPLEMENT", "ENCLOSED ALPHANUMERIC SUPPLEMENT", "ENCLOSEDALPHANUMERICSUPPLEMENT");
            ENCLOSED_ALPHANUMERIC_SUPPLEMENT = unicodeBlock390;
            UnicodeBlock unicodeBlock391 = unicodeBlock390;
            UnicodeBlock unicodeBlock392 = new UnicodeBlock("ENCLOSED_IDEOGRAPHIC_SUPPLEMENT", "ENCLOSED IDEOGRAPHIC SUPPLEMENT", "ENCLOSEDIDEOGRAPHICSUPPLEMENT");
            ENCLOSED_IDEOGRAPHIC_SUPPLEMENT = unicodeBlock392;
            UnicodeBlock unicodeBlock393 = unicodeBlock392;
            UnicodeBlock unicodeBlock394 = new UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS", "MISCELLANEOUS SYMBOLS AND PICTOGRAPHS", "MISCELLANEOUSSYMBOLSANDPICTOGRAPHS");
            MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS = unicodeBlock394;
            UnicodeBlock unicodeBlock395 = new UnicodeBlock("EMOTICONS");
            EMOTICONS = unicodeBlock395;
            UnicodeBlock unicodeBlock396 = unicodeBlock395;
            UnicodeBlock unicodeBlock397 = unicodeBlock394;
            UnicodeBlock unicodeBlock398 = new UnicodeBlock("TRANSPORT_AND_MAP_SYMBOLS", "TRANSPORT AND MAP SYMBOLS", "TRANSPORTANDMAPSYMBOLS");
            TRANSPORT_AND_MAP_SYMBOLS = unicodeBlock398;
            UnicodeBlock unicodeBlock399 = unicodeBlock398;
            UnicodeBlock unicodeBlock400 = new UnicodeBlock("ALCHEMICAL_SYMBOLS", "ALCHEMICAL SYMBOLS", "ALCHEMICALSYMBOLS");
            ALCHEMICAL_SYMBOLS = unicodeBlock400;
            UnicodeBlock unicodeBlock401 = unicodeBlock400;
            UnicodeBlock unicodeBlock402 = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C", "CJK UNIFIED IDEOGRAPHS EXTENSION C", "CJKUNIFIEDIDEOGRAPHSEXTENSIONC");
            CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C = unicodeBlock402;
            UnicodeBlock unicodeBlock403 = unicodeBlock402;
            UnicodeBlock unicodeBlock404 = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D", "CJK UNIFIED IDEOGRAPHS EXTENSION D", "CJKUNIFIEDIDEOGRAPHSEXTENSIOND");
            CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D = unicodeBlock404;
            UnicodeBlock unicodeBlock405 = unicodeBlock404;
            UnicodeBlock unicodeBlock406 = new UnicodeBlock("ARABIC_EXTENDED_A", "ARABIC EXTENDED-A", "ARABICEXTENDED-A");
            ARABIC_EXTENDED_A = unicodeBlock406;
            UnicodeBlock unicodeBlock407 = unicodeBlock406;
            UnicodeBlock unicodeBlock408 = new UnicodeBlock("SUNDANESE_SUPPLEMENT", "SUNDANESE SUPPLEMENT", "SUNDANESESUPPLEMENT");
            SUNDANESE_SUPPLEMENT = unicodeBlock408;
            UnicodeBlock unicodeBlock409 = unicodeBlock408;
            UnicodeBlock unicodeBlock410 = new UnicodeBlock("MEETEI_MAYEK_EXTENSIONS", "MEETEI MAYEK EXTENSIONS", "MEETEIMAYEKEXTENSIONS");
            MEETEI_MAYEK_EXTENSIONS = unicodeBlock410;
            UnicodeBlock unicodeBlock411 = unicodeBlock410;
            UnicodeBlock unicodeBlock412 = new UnicodeBlock("MEROITIC_HIEROGLYPHS", "MEROITIC HIEROGLYPHS", "MEROITICHIEROGLYPHS");
            MEROITIC_HIEROGLYPHS = unicodeBlock412;
            UnicodeBlock unicodeBlock413 = unicodeBlock412;
            UnicodeBlock unicodeBlock414 = new UnicodeBlock("MEROITIC_CURSIVE", "MEROITIC CURSIVE", "MEROITICCURSIVE");
            MEROITIC_CURSIVE = unicodeBlock414;
            UnicodeBlock unicodeBlock415 = unicodeBlock414;
            UnicodeBlock unicodeBlock416 = new UnicodeBlock("SORA_SOMPENG", "SORA SOMPENG", "SORASOMPENG");
            SORA_SOMPENG = unicodeBlock416;
            UnicodeBlock unicodeBlock417 = new UnicodeBlock("CHAKMA");
            CHAKMA = unicodeBlock417;
            UnicodeBlock unicodeBlock418 = new UnicodeBlock("SHARADA");
            SHARADA = unicodeBlock418;
            UnicodeBlock unicodeBlock419 = new UnicodeBlock("TAKRI");
            TAKRI = unicodeBlock419;
            UnicodeBlock unicodeBlock420 = new UnicodeBlock("MIAO");
            MIAO = unicodeBlock420;
            UnicodeBlock unicodeBlock421 = unicodeBlock416;
            UnicodeBlock unicodeBlock422 = new UnicodeBlock("ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS", "ARABIC MATHEMATICAL ALPHABETIC SYMBOLS", "ARABICMATHEMATICALALPHABETICSYMBOLS");
            ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS = unicodeBlock422;
            blocks = new UnicodeBlock[]{unicodeBlock, unicodeBlock2, unicodeBlock3, unicodeBlock4, unicodeBlock5, unicodeBlock6, unicodeBlock7, unicodeBlock8, unicodeBlock9, unicodeBlock158, unicodeBlock10, unicodeBlock11, unicodeBlock51, unicodeBlock157, unicodeBlock238, unicodeBlock156, unicodeBlock245, unicodeBlock242, unicodeBlock244, null, unicodeBlock407, unicodeBlock50, unicodeBlock37, unicodeBlock16, unicodeBlock18, unicodeBlock20, unicodeBlock22, unicodeBlock24, unicodeBlock26, unicodeBlock28, unicodeBlock129, unicodeBlock30, unicodeBlock32, unicodeBlock34, unicodeBlock124, unicodeBlock36, unicodeBlock39, unicodeBlock126, unicodeBlock247, unicodeBlock128, unicodeBlock139, unicodeBlock132, unicodeBlock134, unicodeBlock240, unicodeBlock167, unicodeBlock162, unicodeBlock164, unicodeBlock136, unicodeBlock138, unicodeBlock249, unicodeBlock166, unicodeBlock169, unicodeBlock253, unicodeBlock171, unicodeBlock252, unicodeBlock263, null, unicodeBlock256, unicodeBlock258, unicodeBlock260, unicodeBlock262, unicodeBlock265, null, unicodeBlock409, unicodeBlock267, unicodeBlock173, unicodeBlock269, unicodeBlock273, unicodeBlock41, unicodeBlock43, unicodeBlock45, unicodeBlock47, unicodeBlock49, unicodeBlock122, unicodeBlock120, unicodeBlock57, unicodeBlock56, unicodeBlock59, unicodeBlock61, unicodeBlock63, unicodeBlock65, unicodeBlock67, unicodeBlock69, unicodeBlock71, unicodeBlock73, unicodeBlock77, unicodeBlock76, unicodeBlock175, unicodeBlock177, unicodeBlock141, unicodeBlock179, unicodeBlock181, unicodeBlock183, unicodeBlock185, unicodeBlock272, unicodeBlock277, unicodeBlock276, unicodeBlock281, unicodeBlock280, unicodeBlock283, unicodeBlock285, unicodeBlock287, unicodeBlock143, unicodeBlock145, null, unicodeBlock147, unicodeBlock85, unicodeBlock80, unicodeBlock82, unicodeBlock84, unicodeBlock89, unicodeBlock88, unicodeBlock149, unicodeBlock293, unicodeBlock187, unicodeBlock91, unicodeBlock93, unicodeBlock151, unicodeBlock189, unicodeBlock95, unicodeBlock153, unicodeBlock155, unicodeBlock290, unicodeBlock292, unicodeBlock297, unicodeBlock296, unicodeBlock299, unicodeBlock301, unicodeBlock303, unicodeBlock305, unicodeBlock309, unicodeBlock308, unicodeBlock311, unicodeBlock315, unicodeBlock314, unicodeBlock321, unicodeBlock318, null, unicodeBlock320, unicodeBlock323, unicodeBlock325, unicodeBlock411, unicodeBlock327, null, unicodeBlock329, unicodeBlock97, unicodeBlock331, unicodeBlock233, unicodeBlock235, unicodeBlock237, unicodeBlock99, unicodeBlock101, unicodeBlock103, unicodeBlock105, unicodeBlock191, unicodeBlock333, unicodeBlock107, unicodeBlock109, unicodeBlock111, unicodeBlock113, unicodeBlock117, unicodeBlock116, unicodeBlock193, unicodeBlock195, unicodeBlock197, unicodeBlock335, unicodeBlock337, unicodeBlock343, null, unicodeBlock340, unicodeBlock342, null, unicodeBlock209, unicodeBlock200, null, unicodeBlock202, unicodeBlock345, null, unicodeBlock204, unicodeBlock206, unicodeBlock208, null, unicodeBlock211, unicodeBlock353, null, unicodeBlock348, unicodeBlock350, null, unicodeBlock413, unicodeBlock415, unicodeBlock352, unicodeBlock357, null, unicodeBlock356, unicodeBlock359, unicodeBlock361, null, unicodeBlock363, null, unicodeBlock371, null, unicodeBlock366, unicodeBlock368, unicodeBlock421, unicodeBlock417, null, unicodeBlock418, null, unicodeBlock419, null, unicodeBlock370, unicodeBlock373, null, unicodeBlock375, null, unicodeBlock377, null, unicodeBlock420, null, unicodeBlock379, null, unicodeBlock213, unicodeBlock215, unicodeBlock381, null, unicodeBlock217, unicodeBlock383, null, unicodeBlock219, null, unicodeBlock422, null, unicodeBlock385, unicodeBlock387, unicodeBlock389, unicodeBlock391, unicodeBlock393, unicodeBlock397, unicodeBlock396, null, unicodeBlock399, unicodeBlock401, null, unicodeBlock221, null, unicodeBlock403, unicodeBlock405, null, unicodeBlock225, null, unicodeBlock224, null, unicodeBlock227, null, unicodeBlock229, unicodeBlock231};
        }

        private UnicodeBlock(String str) {
            super(str);
            map.put(str, this);
        }

        private UnicodeBlock(String str, boolean z) {
            super(str);
            if (z) {
                map.put(str, this);
            }
        }

        private UnicodeBlock(String str, String str2) {
            this(str);
            map.put(str2, this);
        }

        private UnicodeBlock(String str, String... strArr) {
            this(str);
            for (String put : strArr) {
                map.put(put, this);
            }
        }

        /* renamed from: of */
        public static UnicodeBlock m1694of(char c) {
            return m1695of((int) c);
        }

        /* renamed from: of */
        public static UnicodeBlock m1695of(int i) {
            if (Character.isValidCodePoint(i)) {
                int length = blockStarts.length;
                int i2 = length / 2;
                int i3 = 0;
                while (length - i3 > 1) {
                    if (i >= blockStarts[i2]) {
                        i3 = i2;
                    } else {
                        length = i2;
                    }
                    i2 = (length + i3) / 2;
                }
                return blocks[i2];
            }
            throw new IllegalArgumentException();
        }

        public static final UnicodeBlock forName(String str) {
            UnicodeBlock unicodeBlock = map.get(str.toUpperCase(Locale.f698US));
            if (unicodeBlock != null) {
                return unicodeBlock;
            }
            throw new IllegalArgumentException();
        }
    }

    public enum UnicodeScript {
        COMMON,
        LATIN,
        GREEK,
        CYRILLIC,
        ARMENIAN,
        HEBREW,
        ARABIC,
        SYRIAC,
        THAANA,
        DEVANAGARI,
        BENGALI,
        GURMUKHI,
        GUJARATI,
        ORIYA,
        TAMIL,
        TELUGU,
        KANNADA,
        MALAYALAM,
        SINHALA,
        THAI,
        LAO,
        TIBETAN,
        MYANMAR,
        GEORGIAN,
        HANGUL,
        ETHIOPIC,
        CHEROKEE,
        CANADIAN_ABORIGINAL,
        OGHAM,
        RUNIC,
        KHMER,
        MONGOLIAN,
        HIRAGANA,
        KATAKANA,
        BOPOMOFO,
        HAN,
        YI,
        OLD_ITALIC,
        GOTHIC,
        DESERET,
        INHERITED,
        TAGALOG,
        HANUNOO,
        BUHID,
        TAGBANWA,
        LIMBU,
        TAI_LE,
        LINEAR_B,
        UGARITIC,
        SHAVIAN,
        OSMANYA,
        CYPRIOT,
        BRAILLE,
        BUGINESE,
        COPTIC,
        NEW_TAI_LUE,
        GLAGOLITIC,
        TIFINAGH,
        SYLOTI_NAGRI,
        OLD_PERSIAN,
        KHAROSHTHI,
        BALINESE,
        CUNEIFORM,
        PHOENICIAN,
        PHAGS_PA,
        NKO,
        SUNDANESE,
        BATAK,
        LEPCHA,
        OL_CHIKI,
        VAI,
        SAURASHTRA,
        KAYAH_LI,
        REJANG,
        LYCIAN,
        CARIAN,
        LYDIAN,
        CHAM,
        TAI_THAM,
        TAI_VIET,
        AVESTAN,
        EGYPTIAN_HIEROGLYPHS,
        SAMARITAN,
        MANDAIC,
        LISU,
        BAMUM,
        JAVANESE,
        MEETEI_MAYEK,
        IMPERIAL_ARAMAIC,
        OLD_SOUTH_ARABIAN,
        INSCRIPTIONAL_PARTHIAN,
        INSCRIPTIONAL_PAHLAVI,
        OLD_TURKIC,
        BRAHMI,
        KAITHI,
        MEROITIC_HIEROGLYPHS,
        MEROITIC_CURSIVE,
        SORA_SOMPENG,
        CHAKMA,
        SHARADA,
        TAKRI,
        MIAO,
        UNKNOWN;
        
        private static HashMap<String, UnicodeScript> aliases;
        private static final int[] scriptStarts = null;
        private static final UnicodeScript[] scripts = null;

        static {
            UnicodeScript unicodeScript;
            UnicodeScript unicodeScript2;
            UnicodeScript unicodeScript3;
            UnicodeScript unicodeScript4;
            UnicodeScript unicodeScript5;
            UnicodeScript unicodeScript6;
            UnicodeScript unicodeScript7;
            UnicodeScript unicodeScript8;
            UnicodeScript unicodeScript9;
            UnicodeScript unicodeScript10;
            UnicodeScript unicodeScript11;
            UnicodeScript unicodeScript12;
            UnicodeScript unicodeScript13;
            UnicodeScript unicodeScript14;
            UnicodeScript unicodeScript15;
            UnicodeScript unicodeScript16;
            UnicodeScript unicodeScript17;
            UnicodeScript unicodeScript18;
            UnicodeScript unicodeScript19;
            UnicodeScript unicodeScript20;
            UnicodeScript unicodeScript21;
            UnicodeScript unicodeScript22;
            UnicodeScript unicodeScript23;
            UnicodeScript unicodeScript24;
            UnicodeScript unicodeScript25;
            UnicodeScript unicodeScript26;
            UnicodeScript unicodeScript27;
            UnicodeScript unicodeScript28;
            UnicodeScript unicodeScript29;
            UnicodeScript unicodeScript30;
            UnicodeScript unicodeScript31;
            UnicodeScript unicodeScript32;
            UnicodeScript unicodeScript33;
            UnicodeScript unicodeScript34;
            UnicodeScript unicodeScript35;
            UnicodeScript unicodeScript36;
            UnicodeScript unicodeScript37;
            UnicodeScript unicodeScript38;
            UnicodeScript unicodeScript39;
            UnicodeScript unicodeScript40;
            UnicodeScript unicodeScript41;
            UnicodeScript unicodeScript42;
            UnicodeScript unicodeScript43;
            UnicodeScript unicodeScript44;
            UnicodeScript unicodeScript45;
            UnicodeScript unicodeScript46;
            UnicodeScript unicodeScript47;
            UnicodeScript unicodeScript48;
            UnicodeScript unicodeScript49;
            UnicodeScript unicodeScript50;
            UnicodeScript unicodeScript51;
            UnicodeScript unicodeScript52;
            UnicodeScript unicodeScript53;
            UnicodeScript unicodeScript54;
            UnicodeScript unicodeScript55;
            UnicodeScript unicodeScript56;
            UnicodeScript unicodeScript57;
            UnicodeScript unicodeScript58;
            UnicodeScript unicodeScript59;
            UnicodeScript unicodeScript60;
            UnicodeScript unicodeScript61;
            UnicodeScript unicodeScript62;
            UnicodeScript unicodeScript63;
            UnicodeScript unicodeScript64;
            UnicodeScript unicodeScript65;
            UnicodeScript unicodeScript66;
            UnicodeScript unicodeScript67;
            UnicodeScript unicodeScript68;
            UnicodeScript unicodeScript69;
            UnicodeScript unicodeScript70;
            UnicodeScript unicodeScript71;
            UnicodeScript unicodeScript72;
            UnicodeScript unicodeScript73;
            UnicodeScript unicodeScript74;
            UnicodeScript unicodeScript75;
            UnicodeScript unicodeScript76;
            UnicodeScript unicodeScript77;
            UnicodeScript unicodeScript78;
            UnicodeScript unicodeScript79;
            UnicodeScript unicodeScript80;
            UnicodeScript unicodeScript81;
            UnicodeScript unicodeScript82;
            UnicodeScript unicodeScript83;
            UnicodeScript unicodeScript84;
            UnicodeScript unicodeScript85;
            UnicodeScript unicodeScript86;
            UnicodeScript unicodeScript87;
            UnicodeScript unicodeScript88;
            UnicodeScript unicodeScript89;
            UnicodeScript unicodeScript90;
            UnicodeScript unicodeScript91;
            UnicodeScript unicodeScript92;
            UnicodeScript unicodeScript93;
            UnicodeScript unicodeScript94;
            UnicodeScript unicodeScript95;
            UnicodeScript unicodeScript96;
            UnicodeScript unicodeScript97;
            UnicodeScript unicodeScript98;
            UnicodeScript unicodeScript99;
            UnicodeScript unicodeScript100;
            UnicodeScript unicodeScript101;
            UnicodeScript unicodeScript102;
            UnicodeScript unicodeScript103;
            int[] iArr = new int[UCharacter.UnicodeBlock.TOTO_ID];
            // fill-array-data instruction
            iArr[0] = 0;
            iArr[1] = 65;
            iArr[2] = 91;
            iArr[3] = 97;
            iArr[4] = 123;
            iArr[5] = 170;
            iArr[6] = 171;
            iArr[7] = 186;
            iArr[8] = 187;
            iArr[9] = 192;
            iArr[10] = 215;
            iArr[11] = 216;
            iArr[12] = 247;
            iArr[13] = 248;
            iArr[14] = 697;
            iArr[15] = 736;
            iArr[16] = 741;
            iArr[17] = 746;
            iArr[18] = 748;
            iArr[19] = 768;
            iArr[20] = 880;
            iArr[21] = 884;
            iArr[22] = 885;
            iArr[23] = 894;
            iArr[24] = 900;
            iArr[25] = 901;
            iArr[26] = 902;
            iArr[27] = 903;
            iArr[28] = 904;
            iArr[29] = 994;
            iArr[30] = 1008;
            iArr[31] = 1024;
            iArr[32] = 1157;
            iArr[33] = 1159;
            iArr[34] = 1329;
            iArr[35] = 1417;
            iArr[36] = 1418;
            iArr[37] = 1425;
            iArr[38] = 1536;
            iArr[39] = 1548;
            iArr[40] = 1549;
            iArr[41] = 1563;
            iArr[42] = 1566;
            iArr[43] = 1567;
            iArr[44] = 1568;
            iArr[45] = 1600;
            iArr[46] = 1601;
            iArr[47] = 1611;
            iArr[48] = 1622;
            iArr[49] = 1632;
            iArr[50] = 1642;
            iArr[51] = 1648;
            iArr[52] = 1649;
            iArr[53] = 1757;
            iArr[54] = 1758;
            iArr[55] = 1792;
            iArr[56] = 1872;
            iArr[57] = 1920;
            iArr[58] = 1984;
            iArr[59] = 2048;
            iArr[60] = 2112;
            iArr[61] = 2208;
            iArr[62] = 2304;
            iArr[63] = 2385;
            iArr[64] = 2387;
            iArr[65] = 2404;
            iArr[66] = 2406;
            iArr[67] = 2433;
            iArr[68] = 2561;
            iArr[69] = 2689;
            iArr[70] = 2817;
            iArr[71] = 2946;
            iArr[72] = 3073;
            iArr[73] = 3202;
            iArr[74] = 3330;
            iArr[75] = 3458;
            iArr[76] = 3585;
            iArr[77] = 3647;
            iArr[78] = 3648;
            iArr[79] = 3713;
            iArr[80] = 3840;
            iArr[81] = 4053;
            iArr[82] = 4057;
            iArr[83] = 4096;
            iArr[84] = 4256;
            iArr[85] = 4347;
            iArr[86] = 4348;
            iArr[87] = 4352;
            iArr[88] = 4608;
            iArr[89] = 5024;
            iArr[90] = 5120;
            iArr[91] = 5760;
            iArr[92] = 5792;
            iArr[93] = 5867;
            iArr[94] = 5870;
            iArr[95] = 5888;
            iArr[96] = 5920;
            iArr[97] = 5941;
            iArr[98] = 5952;
            iArr[99] = 5984;
            iArr[100] = 6016;
            iArr[101] = 6144;
            iArr[102] = 6146;
            iArr[103] = 6148;
            iArr[104] = 6149;
            iArr[105] = 6150;
            iArr[106] = 6320;
            iArr[107] = 6400;
            iArr[108] = 6480;
            iArr[109] = 6528;
            iArr[110] = 6624;
            iArr[111] = 6656;
            iArr[112] = 6688;
            iArr[113] = 6912;
            iArr[114] = 7040;
            iArr[115] = 7104;
            iArr[116] = 7168;
            iArr[117] = 7248;
            iArr[118] = 7360;
            iArr[119] = 7376;
            iArr[120] = 7379;
            iArr[121] = 7380;
            iArr[122] = 7393;
            iArr[123] = 7394;
            iArr[124] = 7401;
            iArr[125] = 7405;
            iArr[126] = 7406;
            iArr[127] = 7412;
            iArr[128] = 7413;
            iArr[129] = 7424;
            iArr[130] = 7462;
            iArr[131] = 7467;
            iArr[132] = 7468;
            iArr[133] = 7517;
            iArr[134] = 7522;
            iArr[135] = 7526;
            iArr[136] = 7531;
            iArr[137] = 7544;
            iArr[138] = 7545;
            iArr[139] = 7615;
            iArr[140] = 7616;
            iArr[141] = 7680;
            iArr[142] = 7936;
            iArr[143] = 8192;
            iArr[144] = 8204;
            iArr[145] = 8206;
            iArr[146] = 8305;
            iArr[147] = 8308;
            iArr[148] = 8319;
            iArr[149] = 8320;
            iArr[150] = 8336;
            iArr[151] = 8352;
            iArr[152] = 8400;
            iArr[153] = 8448;
            iArr[154] = 8486;
            iArr[155] = 8487;
            iArr[156] = 8490;
            iArr[157] = 8492;
            iArr[158] = 8498;
            iArr[159] = 8499;
            iArr[160] = 8526;
            iArr[161] = 8527;
            iArr[162] = 8544;
            iArr[163] = 8585;
            iArr[164] = 10240;
            iArr[165] = 10496;
            iArr[166] = 11264;
            iArr[167] = 11360;
            iArr[168] = 11392;
            iArr[169] = 11520;
            iArr[170] = 11568;
            iArr[171] = 11648;
            iArr[172] = 11744;
            iArr[173] = 11776;
            iArr[174] = 11904;
            iArr[175] = 12272;
            iArr[176] = 12293;
            iArr[177] = 12294;
            iArr[178] = 12295;
            iArr[179] = 12296;
            iArr[180] = 12321;
            iArr[181] = 12330;
            iArr[182] = 12334;
            iArr[183] = 12336;
            iArr[184] = 12344;
            iArr[185] = 12348;
            iArr[186] = 12353;
            iArr[187] = 12441;
            iArr[188] = 12443;
            iArr[189] = 12445;
            iArr[190] = 12448;
            iArr[191] = 12449;
            iArr[192] = 12539;
            iArr[193] = 12541;
            iArr[194] = 12549;
            iArr[195] = 12593;
            iArr[196] = 12688;
            iArr[197] = 12704;
            iArr[198] = 12736;
            iArr[199] = 12784;
            iArr[200] = 12800;
            iArr[201] = 12832;
            iArr[202] = 12896;
            iArr[203] = 12927;
            iArr[204] = 13008;
            iArr[205] = 13144;
            iArr[206] = 13312;
            iArr[207] = 19904;
            iArr[208] = 19968;
            iArr[209] = 40960;
            iArr[210] = 42192;
            iArr[211] = 42240;
            iArr[212] = 42560;
            iArr[213] = 42656;
            iArr[214] = 42752;
            iArr[215] = 42786;
            iArr[216] = 42888;
            iArr[217] = 42891;
            iArr[218] = 43008;
            iArr[219] = 43056;
            iArr[220] = 43072;
            iArr[221] = 43136;
            iArr[222] = 43232;
            iArr[223] = 43264;
            iArr[224] = 43312;
            iArr[225] = 43360;
            iArr[226] = 43392;
            iArr[227] = 43520;
            iArr[228] = 43616;
            iArr[229] = 43648;
            iArr[230] = 43744;
            iArr[231] = 43777;
            iArr[232] = 43968;
            iArr[233] = 44032;
            iArr[234] = 55292;
            iArr[235] = 63744;
            iArr[236] = 64256;
            iArr[237] = 64275;
            iArr[238] = 64285;
            iArr[239] = 64336;
            iArr[240] = 64830;
            iArr[241] = 64848;
            iArr[242] = 65021;
            iArr[243] = 65024;
            iArr[244] = 65040;
            iArr[245] = 65056;
            iArr[246] = 65072;
            iArr[247] = 65136;
            iArr[248] = 65279;
            iArr[249] = 65313;
            iArr[250] = 65339;
            iArr[251] = 65345;
            iArr[252] = 65371;
            iArr[253] = 65382;
            iArr[254] = 65392;
            iArr[255] = 65393;
            iArr[256] = 65438;
            iArr[257] = 65440;
            iArr[258] = 65504;
            iArr[259] = 65536;
            iArr[260] = 65792;
            iArr[261] = 65856;
            iArr[262] = 65936;
            iArr[263] = 66045;
            iArr[264] = 66176;
            iArr[265] = 66208;
            iArr[266] = 66304;
            iArr[267] = 66352;
            iArr[268] = 66432;
            iArr[269] = 66464;
            iArr[270] = 66560;
            iArr[271] = 66640;
            iArr[272] = 66688;
            iArr[273] = 67584;
            iArr[274] = 67648;
            iArr[275] = 67840;
            iArr[276] = 67872;
            iArr[277] = 67968;
            iArr[278] = 68000;
            iArr[279] = 68096;
            iArr[280] = 68192;
            iArr[281] = 68352;
            iArr[282] = 68416;
            iArr[283] = 68448;
            iArr[284] = 68608;
            iArr[285] = 69216;
            iArr[286] = 69632;
            iArr[287] = 69760;
            iArr[288] = 69840;
            iArr[289] = 69888;
            iArr[290] = 70016;
            iArr[291] = 71296;
            iArr[292] = 73728;
            iArr[293] = 77824;
            iArr[294] = 92160;
            iArr[295] = 93952;
            iArr[296] = 110592;
            iArr[297] = 110593;
            iArr[298] = 118784;
            iArr[299] = 119143;
            iArr[300] = 119146;
            iArr[301] = 119163;
            iArr[302] = 119171;
            iArr[303] = 119173;
            iArr[304] = 119180;
            iArr[305] = 119210;
            iArr[306] = 119214;
            iArr[307] = 119296;
            iArr[308] = 119552;
            iArr[309] = 126464;
            iArr[310] = 126976;
            iArr[311] = 127488;
            iArr[312] = 127489;
            iArr[313] = 131072;
            iArr[314] = 917505;
            iArr[315] = 917760;
            iArr[316] = 918000;
            scriptStarts = iArr;
            UnicodeScript[] unicodeScriptArr = new UnicodeScript[UCharacter.UnicodeBlock.TOTO_ID];
            unicodeScriptArr[0] = unicodeScript;
            unicodeScriptArr[1] = unicodeScript2;
            unicodeScriptArr[2] = unicodeScript;
            unicodeScriptArr[3] = unicodeScript2;
            unicodeScriptArr[4] = unicodeScript;
            unicodeScriptArr[5] = unicodeScript2;
            unicodeScriptArr[6] = unicodeScript;
            unicodeScriptArr[7] = unicodeScript2;
            unicodeScriptArr[8] = unicodeScript;
            unicodeScriptArr[9] = unicodeScript2;
            unicodeScriptArr[10] = unicodeScript;
            unicodeScriptArr[11] = unicodeScript2;
            unicodeScriptArr[12] = unicodeScript;
            unicodeScriptArr[13] = unicodeScript2;
            unicodeScriptArr[14] = unicodeScript;
            unicodeScriptArr[15] = unicodeScript2;
            unicodeScriptArr[16] = unicodeScript;
            unicodeScriptArr[17] = unicodeScript35;
            unicodeScriptArr[18] = unicodeScript;
            unicodeScriptArr[19] = unicodeScript41;
            unicodeScriptArr[20] = unicodeScript3;
            unicodeScriptArr[21] = unicodeScript;
            unicodeScriptArr[22] = unicodeScript3;
            unicodeScriptArr[23] = unicodeScript;
            unicodeScriptArr[24] = unicodeScript3;
            unicodeScriptArr[25] = unicodeScript;
            unicodeScriptArr[26] = unicodeScript3;
            unicodeScriptArr[27] = unicodeScript;
            unicodeScriptArr[28] = unicodeScript3;
            unicodeScriptArr[29] = unicodeScript55;
            unicodeScriptArr[30] = unicodeScript3;
            unicodeScriptArr[31] = unicodeScript4;
            unicodeScriptArr[32] = unicodeScript41;
            unicodeScriptArr[33] = unicodeScript4;
            unicodeScriptArr[34] = unicodeScript5;
            unicodeScriptArr[35] = unicodeScript;
            unicodeScriptArr[36] = unicodeScript5;
            unicodeScriptArr[37] = unicodeScript6;
            unicodeScriptArr[38] = unicodeScript7;
            unicodeScriptArr[39] = unicodeScript;
            unicodeScriptArr[40] = unicodeScript7;
            unicodeScriptArr[41] = unicodeScript;
            unicodeScriptArr[42] = unicodeScript7;
            unicodeScriptArr[43] = unicodeScript;
            unicodeScriptArr[44] = unicodeScript7;
            unicodeScriptArr[45] = unicodeScript;
            unicodeScriptArr[46] = unicodeScript7;
            unicodeScriptArr[47] = unicodeScript41;
            unicodeScriptArr[48] = unicodeScript7;
            unicodeScriptArr[49] = unicodeScript;
            unicodeScriptArr[50] = unicodeScript7;
            unicodeScriptArr[51] = unicodeScript41;
            unicodeScriptArr[52] = unicodeScript7;
            unicodeScriptArr[53] = unicodeScript;
            unicodeScriptArr[54] = unicodeScript7;
            unicodeScriptArr[55] = unicodeScript8;
            unicodeScriptArr[56] = unicodeScript7;
            unicodeScriptArr[57] = unicodeScript9;
            unicodeScriptArr[58] = unicodeScript66;
            unicodeScriptArr[59] = unicodeScript83;
            unicodeScriptArr[60] = unicodeScript84;
            unicodeScriptArr[61] = unicodeScript7;
            unicodeScriptArr[62] = unicodeScript10;
            unicodeScriptArr[63] = unicodeScript41;
            unicodeScriptArr[64] = unicodeScript10;
            unicodeScriptArr[65] = unicodeScript;
            unicodeScriptArr[66] = unicodeScript10;
            unicodeScriptArr[67] = unicodeScript11;
            unicodeScriptArr[68] = unicodeScript12;
            unicodeScriptArr[69] = unicodeScript13;
            unicodeScriptArr[70] = unicodeScript14;
            unicodeScriptArr[71] = unicodeScript15;
            unicodeScriptArr[72] = unicodeScript16;
            unicodeScriptArr[73] = unicodeScript17;
            unicodeScriptArr[74] = unicodeScript18;
            unicodeScriptArr[75] = unicodeScript19;
            unicodeScriptArr[76] = unicodeScript20;
            unicodeScriptArr[77] = unicodeScript;
            unicodeScriptArr[78] = unicodeScript20;
            unicodeScriptArr[79] = unicodeScript21;
            unicodeScriptArr[80] = unicodeScript22;
            unicodeScriptArr[81] = unicodeScript;
            unicodeScriptArr[82] = unicodeScript22;
            unicodeScriptArr[83] = unicodeScript23;
            unicodeScriptArr[84] = unicodeScript24;
            unicodeScriptArr[85] = unicodeScript;
            unicodeScriptArr[86] = unicodeScript24;
            unicodeScriptArr[87] = unicodeScript25;
            unicodeScriptArr[88] = unicodeScript26;
            unicodeScriptArr[89] = unicodeScript27;
            unicodeScriptArr[90] = unicodeScript28;
            unicodeScriptArr[91] = unicodeScript29;
            unicodeScriptArr[92] = unicodeScript30;
            unicodeScriptArr[93] = unicodeScript;
            unicodeScriptArr[94] = unicodeScript30;
            unicodeScriptArr[95] = unicodeScript42;
            unicodeScriptArr[96] = unicodeScript43;
            unicodeScriptArr[97] = unicodeScript;
            unicodeScriptArr[98] = unicodeScript44;
            unicodeScriptArr[99] = unicodeScript45;
            unicodeScriptArr[100] = unicodeScript31;
            unicodeScriptArr[101] = unicodeScript32;
            unicodeScriptArr[102] = unicodeScript;
            unicodeScriptArr[103] = unicodeScript32;
            unicodeScriptArr[104] = unicodeScript;
            unicodeScriptArr[105] = unicodeScript32;
            unicodeScriptArr[106] = unicodeScript28;
            unicodeScriptArr[107] = unicodeScript46;
            unicodeScriptArr[108] = unicodeScript47;
            unicodeScriptArr[109] = unicodeScript56;
            unicodeScriptArr[110] = unicodeScript31;
            unicodeScriptArr[111] = unicodeScript54;
            unicodeScriptArr[112] = unicodeScript79;
            unicodeScriptArr[113] = unicodeScript62;
            unicodeScriptArr[114] = unicodeScript67;
            unicodeScriptArr[115] = unicodeScript68;
            unicodeScriptArr[116] = unicodeScript69;
            unicodeScriptArr[117] = unicodeScript70;
            unicodeScriptArr[118] = unicodeScript67;
            unicodeScriptArr[119] = unicodeScript41;
            unicodeScriptArr[120] = unicodeScript;
            unicodeScriptArr[121] = unicodeScript41;
            unicodeScriptArr[122] = unicodeScript;
            unicodeScriptArr[123] = unicodeScript41;
            unicodeScriptArr[124] = unicodeScript;
            unicodeScriptArr[125] = unicodeScript41;
            unicodeScriptArr[126] = unicodeScript;
            unicodeScriptArr[127] = unicodeScript41;
            unicodeScriptArr[128] = unicodeScript;
            unicodeScriptArr[129] = unicodeScript2;
            unicodeScriptArr[130] = unicodeScript3;
            unicodeScriptArr[131] = unicodeScript4;
            unicodeScriptArr[132] = unicodeScript2;
            unicodeScriptArr[133] = unicodeScript3;
            unicodeScriptArr[134] = unicodeScript2;
            unicodeScriptArr[135] = unicodeScript3;
            unicodeScriptArr[136] = unicodeScript2;
            unicodeScriptArr[137] = unicodeScript4;
            unicodeScriptArr[138] = unicodeScript2;
            unicodeScriptArr[139] = unicodeScript3;
            unicodeScriptArr[140] = unicodeScript41;
            unicodeScriptArr[141] = unicodeScript2;
            unicodeScriptArr[142] = unicodeScript3;
            unicodeScriptArr[143] = unicodeScript;
            unicodeScriptArr[144] = unicodeScript41;
            unicodeScriptArr[145] = unicodeScript;
            unicodeScriptArr[146] = unicodeScript2;
            unicodeScriptArr[147] = unicodeScript;
            unicodeScriptArr[148] = unicodeScript2;
            unicodeScriptArr[149] = unicodeScript;
            unicodeScriptArr[150] = unicodeScript2;
            unicodeScriptArr[151] = unicodeScript;
            unicodeScriptArr[152] = unicodeScript41;
            unicodeScriptArr[153] = unicodeScript;
            unicodeScriptArr[154] = unicodeScript3;
            unicodeScriptArr[155] = unicodeScript;
            unicodeScriptArr[156] = unicodeScript2;
            unicodeScriptArr[157] = unicodeScript;
            unicodeScriptArr[158] = unicodeScript2;
            unicodeScriptArr[159] = unicodeScript;
            unicodeScriptArr[160] = unicodeScript2;
            unicodeScriptArr[161] = unicodeScript;
            unicodeScriptArr[162] = unicodeScript2;
            unicodeScriptArr[163] = unicodeScript;
            unicodeScriptArr[164] = unicodeScript53;
            unicodeScriptArr[165] = unicodeScript;
            unicodeScriptArr[166] = unicodeScript57;
            unicodeScriptArr[167] = unicodeScript2;
            unicodeScriptArr[168] = unicodeScript55;
            unicodeScriptArr[169] = unicodeScript24;
            unicodeScriptArr[170] = unicodeScript58;
            unicodeScriptArr[171] = unicodeScript26;
            unicodeScriptArr[172] = unicodeScript4;
            unicodeScriptArr[173] = unicodeScript;
            unicodeScriptArr[174] = unicodeScript36;
            unicodeScriptArr[175] = unicodeScript;
            unicodeScriptArr[176] = unicodeScript36;
            unicodeScriptArr[177] = unicodeScript;
            unicodeScriptArr[178] = unicodeScript36;
            unicodeScriptArr[179] = unicodeScript;
            unicodeScriptArr[180] = unicodeScript36;
            unicodeScriptArr[181] = unicodeScript41;
            unicodeScriptArr[182] = unicodeScript25;
            unicodeScriptArr[183] = unicodeScript;
            unicodeScriptArr[184] = unicodeScript36;
            unicodeScriptArr[185] = unicodeScript;
            unicodeScriptArr[186] = unicodeScript33;
            unicodeScriptArr[187] = unicodeScript41;
            unicodeScriptArr[188] = unicodeScript;
            unicodeScriptArr[189] = unicodeScript33;
            unicodeScriptArr[190] = unicodeScript;
            unicodeScriptArr[191] = unicodeScript34;
            unicodeScriptArr[192] = unicodeScript;
            unicodeScriptArr[193] = unicodeScript34;
            unicodeScriptArr[194] = unicodeScript35;
            unicodeScriptArr[195] = unicodeScript25;
            unicodeScriptArr[196] = unicodeScript;
            unicodeScriptArr[197] = unicodeScript35;
            unicodeScriptArr[198] = unicodeScript;
            unicodeScriptArr[199] = unicodeScript34;
            unicodeScriptArr[200] = unicodeScript25;
            unicodeScriptArr[201] = unicodeScript;
            unicodeScriptArr[202] = unicodeScript25;
            unicodeScriptArr[203] = unicodeScript;
            unicodeScriptArr[204] = unicodeScript34;
            unicodeScriptArr[205] = unicodeScript;
            unicodeScriptArr[206] = unicodeScript36;
            unicodeScriptArr[207] = unicodeScript;
            unicodeScriptArr[208] = unicodeScript36;
            unicodeScriptArr[209] = unicodeScript37;
            unicodeScriptArr[210] = unicodeScript85;
            unicodeScriptArr[211] = unicodeScript71;
            unicodeScriptArr[212] = unicodeScript4;
            unicodeScriptArr[213] = unicodeScript86;
            unicodeScriptArr[214] = unicodeScript;
            unicodeScriptArr[215] = unicodeScript2;
            unicodeScriptArr[216] = unicodeScript;
            unicodeScriptArr[217] = unicodeScript2;
            unicodeScriptArr[218] = unicodeScript59;
            unicodeScriptArr[219] = unicodeScript;
            unicodeScriptArr[220] = unicodeScript65;
            unicodeScriptArr[221] = unicodeScript72;
            unicodeScriptArr[222] = unicodeScript10;
            unicodeScriptArr[223] = unicodeScript73;
            unicodeScriptArr[224] = unicodeScript74;
            unicodeScriptArr[225] = unicodeScript25;
            unicodeScriptArr[226] = unicodeScript87;
            unicodeScriptArr[227] = unicodeScript78;
            unicodeScriptArr[228] = unicodeScript23;
            unicodeScriptArr[229] = unicodeScript80;
            unicodeScriptArr[230] = unicodeScript88;
            unicodeScriptArr[231] = unicodeScript26;
            unicodeScriptArr[232] = unicodeScript88;
            unicodeScriptArr[233] = unicodeScript25;
            unicodeScriptArr[234] = unicodeScript102;
            unicodeScriptArr[235] = unicodeScript36;
            unicodeScriptArr[236] = unicodeScript2;
            unicodeScriptArr[237] = unicodeScript5;
            unicodeScriptArr[238] = unicodeScript6;
            unicodeScriptArr[239] = unicodeScript7;
            unicodeScriptArr[240] = unicodeScript;
            unicodeScriptArr[241] = unicodeScript7;
            unicodeScriptArr[242] = unicodeScript;
            unicodeScriptArr[243] = unicodeScript41;
            unicodeScriptArr[244] = unicodeScript;
            unicodeScriptArr[245] = unicodeScript41;
            unicodeScriptArr[246] = unicodeScript;
            unicodeScriptArr[247] = unicodeScript7;
            unicodeScriptArr[248] = unicodeScript;
            unicodeScriptArr[249] = unicodeScript2;
            unicodeScriptArr[250] = unicodeScript;
            unicodeScriptArr[251] = unicodeScript2;
            unicodeScriptArr[252] = unicodeScript;
            unicodeScriptArr[253] = unicodeScript34;
            unicodeScriptArr[254] = unicodeScript;
            unicodeScriptArr[255] = unicodeScript34;
            unicodeScriptArr[256] = unicodeScript;
            unicodeScriptArr[257] = unicodeScript25;
            unicodeScriptArr[258] = unicodeScript;
            unicodeScriptArr[259] = unicodeScript48;
            unicodeScriptArr[260] = unicodeScript;
            unicodeScriptArr[261] = unicodeScript3;
            unicodeScriptArr[262] = unicodeScript;
            unicodeScriptArr[263] = unicodeScript41;
            unicodeScriptArr[264] = unicodeScript75;
            unicodeScriptArr[265] = unicodeScript76;
            unicodeScriptArr[266] = unicodeScript38;
            unicodeScriptArr[267] = unicodeScript39;
            unicodeScriptArr[268] = unicodeScript49;
            unicodeScriptArr[269] = unicodeScript60;
            unicodeScriptArr[270] = unicodeScript40;
            unicodeScriptArr[271] = unicodeScript50;
            unicodeScriptArr[272] = unicodeScript51;
            unicodeScriptArr[273] = unicodeScript52;
            unicodeScriptArr[274] = unicodeScript89;
            unicodeScriptArr[275] = unicodeScript64;
            unicodeScriptArr[276] = unicodeScript77;
            unicodeScriptArr[277] = unicodeScript96;
            unicodeScriptArr[278] = unicodeScript97;
            unicodeScriptArr[279] = unicodeScript61;
            unicodeScriptArr[280] = unicodeScript90;
            unicodeScriptArr[281] = unicodeScript81;
            unicodeScriptArr[282] = unicodeScript91;
            unicodeScriptArr[283] = unicodeScript92;
            unicodeScriptArr[284] = unicodeScript93;
            unicodeScriptArr[285] = unicodeScript7;
            unicodeScriptArr[286] = unicodeScript94;
            unicodeScriptArr[287] = unicodeScript95;
            unicodeScriptArr[288] = unicodeScript98;
            unicodeScriptArr[289] = unicodeScript99;
            unicodeScriptArr[290] = unicodeScript100;
            unicodeScriptArr[291] = unicodeScript101;
            unicodeScriptArr[292] = unicodeScript63;
            unicodeScriptArr[293] = unicodeScript82;
            unicodeScriptArr[294] = unicodeScript86;
            unicodeScriptArr[295] = unicodeScript103;
            unicodeScriptArr[296] = unicodeScript34;
            unicodeScriptArr[297] = unicodeScript33;
            unicodeScriptArr[298] = unicodeScript;
            unicodeScriptArr[299] = unicodeScript41;
            unicodeScriptArr[300] = unicodeScript;
            unicodeScriptArr[301] = unicodeScript41;
            unicodeScriptArr[302] = unicodeScript;
            unicodeScriptArr[303] = unicodeScript41;
            unicodeScriptArr[304] = unicodeScript;
            unicodeScriptArr[305] = unicodeScript41;
            unicodeScriptArr[306] = unicodeScript;
            unicodeScriptArr[307] = unicodeScript3;
            unicodeScriptArr[308] = unicodeScript;
            unicodeScriptArr[309] = unicodeScript7;
            unicodeScriptArr[310] = unicodeScript;
            unicodeScriptArr[311] = unicodeScript33;
            unicodeScriptArr[312] = unicodeScript;
            unicodeScriptArr[313] = unicodeScript36;
            unicodeScriptArr[314] = unicodeScript;
            unicodeScriptArr[315] = unicodeScript41;
            unicodeScriptArr[316] = unicodeScript102;
            scripts = unicodeScriptArr;
            HashMap<String, UnicodeScript> hashMap = new HashMap<>(128);
            aliases = hashMap;
            hashMap.put("ARAB", unicodeScript7);
            aliases.put("ARMI", unicodeScript89);
            aliases.put("ARMN", unicodeScript5);
            aliases.put("AVST", unicodeScript81);
            aliases.put("BALI", unicodeScript62);
            aliases.put("BAMU", unicodeScript86);
            aliases.put("BATK", unicodeScript68);
            aliases.put("BENG", unicodeScript11);
            aliases.put("BOPO", unicodeScript35);
            aliases.put("BRAI", unicodeScript53);
            aliases.put("BRAH", unicodeScript94);
            aliases.put("BUGI", unicodeScript54);
            aliases.put("BUHD", unicodeScript44);
            aliases.put("CAKM", unicodeScript99);
            aliases.put("CANS", unicodeScript28);
            aliases.put("CARI", unicodeScript76);
            aliases.put("CHAM", unicodeScript78);
            aliases.put("CHER", unicodeScript27);
            aliases.put("COPT", unicodeScript55);
            aliases.put("CPRT", unicodeScript52);
            aliases.put("CYRL", unicodeScript4);
            aliases.put("DEVA", unicodeScript10);
            aliases.put("DSRT", unicodeScript40);
            aliases.put("EGYP", unicodeScript82);
            aliases.put("ETHI", unicodeScript26);
            aliases.put("GEOR", unicodeScript24);
            aliases.put("GLAG", unicodeScript57);
            aliases.put("GOTH", unicodeScript39);
            aliases.put("GREK", unicodeScript3);
            aliases.put("GUJR", unicodeScript13);
            aliases.put("GURU", unicodeScript12);
            aliases.put("HANG", unicodeScript25);
            aliases.put("HANI", unicodeScript36);
            aliases.put("HANO", unicodeScript43);
            aliases.put("HEBR", unicodeScript6);
            aliases.put("HIRA", unicodeScript33);
            aliases.put("ITAL", unicodeScript38);
            aliases.put("JAVA", unicodeScript87);
            aliases.put("KALI", unicodeScript73);
            aliases.put("KANA", unicodeScript34);
            aliases.put("KHAR", unicodeScript61);
            aliases.put("KHMR", unicodeScript31);
            aliases.put("KNDA", unicodeScript17);
            aliases.put("KTHI", unicodeScript95);
            aliases.put("LANA", unicodeScript79);
            aliases.put("LAOO", unicodeScript21);
            aliases.put("LATN", unicodeScript2);
            aliases.put("LEPC", unicodeScript69);
            aliases.put("LIMB", unicodeScript46);
            aliases.put("LINB", unicodeScript48);
            aliases.put("LISU", unicodeScript85);
            aliases.put("LYCI", unicodeScript75);
            aliases.put("LYDI", unicodeScript77);
            aliases.put("MAND", unicodeScript84);
            aliases.put("MERC", unicodeScript97);
            aliases.put("MERO", unicodeScript96);
            aliases.put("MLYM", unicodeScript18);
            aliases.put("MONG", unicodeScript32);
            aliases.put("MTEI", unicodeScript88);
            aliases.put("MYMR", unicodeScript23);
            aliases.put("NKOO", unicodeScript66);
            aliases.put("OGAM", unicodeScript29);
            aliases.put("OLCK", unicodeScript70);
            aliases.put("ORKH", unicodeScript93);
            aliases.put("ORYA", unicodeScript14);
            aliases.put("OSMA", unicodeScript51);
            aliases.put("PHAG", unicodeScript65);
            aliases.put("PLRD", unicodeScript103);
            aliases.put("PHLI", unicodeScript92);
            aliases.put("PHNX", unicodeScript64);
            aliases.put("PRTI", unicodeScript91);
            aliases.put("RJNG", unicodeScript74);
            aliases.put("RUNR", unicodeScript30);
            aliases.put("SAMR", unicodeScript83);
            aliases.put("SARB", unicodeScript90);
            aliases.put("SAUR", unicodeScript72);
            aliases.put("SHAW", unicodeScript50);
            aliases.put("SHRD", unicodeScript100);
            aliases.put("SINH", unicodeScript19);
            aliases.put("SORA", unicodeScript98);
            aliases.put("SUND", unicodeScript67);
            aliases.put("SYLO", unicodeScript59);
            aliases.put("SYRC", unicodeScript8);
            aliases.put("TAGB", unicodeScript45);
            aliases.put("TALE", unicodeScript47);
            aliases.put("TAKR", unicodeScript101);
            aliases.put("TALU", unicodeScript56);
            aliases.put("TAML", unicodeScript15);
            aliases.put("TAVT", unicodeScript80);
            aliases.put("TELU", unicodeScript16);
            aliases.put("TFNG", unicodeScript58);
            aliases.put("TGLG", unicodeScript42);
            aliases.put("THAA", unicodeScript9);
            aliases.put("THAI", unicodeScript20);
            aliases.put("TIBT", unicodeScript22);
            aliases.put("UGAR", unicodeScript49);
            aliases.put("VAII", unicodeScript71);
            aliases.put("XPEO", unicodeScript60);
            aliases.put("XSUX", unicodeScript63);
            aliases.put("YIII", unicodeScript37);
            aliases.put("ZINH", unicodeScript41);
            aliases.put("ZYYY", unicodeScript);
            aliases.put(DateFormat.ABBR_UTC_TZ, unicodeScript102);
        }

        /* renamed from: of */
        public static UnicodeScript m1696of(int i) {
            if (!Character.isValidCodePoint(i)) {
                throw new IllegalArgumentException();
            } else if (Character.getType(i) == 0) {
                return UNKNOWN;
            } else {
                int binarySearch = Arrays.binarySearch(scriptStarts, i);
                if (binarySearch < 0) {
                    binarySearch = (-binarySearch) - 2;
                }
                return scripts[binarySearch];
            }
        }

        public static final UnicodeScript forName(String str) {
            String upperCase = str.toUpperCase(Locale.ENGLISH);
            UnicodeScript unicodeScript = aliases.get(upperCase);
            if (unicodeScript != null) {
                return unicodeScript;
            }
            return valueOf(upperCase);
        }
    }

    public Character(char c) {
        this.value = c;
    }

    private static class CharacterCache {
        static final Character[] cache = new Character[128];

        private CharacterCache() {
        }

        static {
            int i = 0;
            while (true) {
                Character[] chArr = cache;
                if (i < chArr.length) {
                    chArr[i] = new Character((char) i);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public static Character valueOf(char c) {
        if (c <= 127) {
            return CharacterCache.cache[c];
        }
        return new Character(c);
    }

    public char charValue() {
        return this.value;
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Character) || this.value != ((Character) obj).charValue()) {
            return false;
        }
        return true;
    }

    public String toString() {
        return String.valueOf(new char[]{this.value});
    }

    public static String toString(char c) {
        return String.valueOf(c);
    }

    public static boolean isSurrogatePair(char c, char c2) {
        return isHighSurrogate(c) && isLowSurrogate(c2);
    }

    public static int codePointAt(CharSequence charSequence, int i) {
        int i2;
        char charAt = charSequence.charAt(i);
        if (isHighSurrogate(charAt) && (i2 = i + 1) < charSequence.length()) {
            char charAt2 = charSequence.charAt(i2);
            if (isLowSurrogate(charAt2)) {
                return toCodePoint(charAt, charAt2);
            }
        }
        return charAt;
    }

    public static int codePointAt(char[] cArr, int i) {
        return codePointAtImpl(cArr, i, cArr.length);
    }

    public static int codePointAt(char[] cArr, int i, int i2) {
        if (i < i2 && i2 >= 0 && i2 <= cArr.length) {
            return codePointAtImpl(cArr, i, i2);
        }
        throw new IndexOutOfBoundsException();
    }

    static int codePointAtImpl(char[] cArr, int i, int i2) {
        int i3;
        char c = cArr[i];
        if (isHighSurrogate(c) && (i3 = i + 1) < i2) {
            char c2 = cArr[i3];
            if (isLowSurrogate(c2)) {
                return toCodePoint(c, c2);
            }
        }
        return c;
    }

    public static int codePointBefore(CharSequence charSequence, int i) {
        int i2 = i - 1;
        char charAt = charSequence.charAt(i2);
        if (isLowSurrogate(charAt) && i2 > 0) {
            char charAt2 = charSequence.charAt(i2 - 1);
            if (isHighSurrogate(charAt2)) {
                return toCodePoint(charAt2, charAt);
            }
        }
        return charAt;
    }

    public static int codePointBefore(char[] cArr, int i) {
        return codePointBeforeImpl(cArr, i, 0);
    }

    public static int codePointBefore(char[] cArr, int i, int i2) {
        if (i > i2 && i2 >= 0 && i2 < cArr.length) {
            return codePointBeforeImpl(cArr, i, i2);
        }
        throw new IndexOutOfBoundsException();
    }

    static int codePointBeforeImpl(char[] cArr, int i, int i2) {
        int i3 = i - 1;
        char c = cArr[i3];
        if (isLowSurrogate(c) && i3 > i2) {
            char c2 = cArr[i3 - 1];
            if (isHighSurrogate(c2)) {
                return toCodePoint(c2, c);
            }
        }
        return c;
    }

    public static int toChars(int i, char[] cArr, int i2) {
        if (isBmpCodePoint(i)) {
            cArr[i2] = (char) i;
            return 1;
        } else if (isValidCodePoint(i)) {
            toSurrogates(i, cArr, i2);
            return 2;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static char[] toChars(int i) {
        if (isBmpCodePoint(i)) {
            return new char[]{(char) i};
        } else if (isValidCodePoint(i)) {
            char[] cArr = new char[2];
            toSurrogates(i, cArr, 0);
            return cArr;
        } else {
            throw new IllegalArgumentException();
        }
    }

    static void toSurrogates(int i, char[] cArr, int i2) {
        cArr[i2 + 1] = lowSurrogate(i);
        cArr[i2] = highSurrogate(i);
    }

    public static int codePointCount(CharSequence charSequence, int i, int i2) {
        int length = charSequence.length();
        if (i < 0 || i2 > length || i > i2) {
            throw new IndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        while (i < i2) {
            int i4 = i + 1;
            if (!isHighSurrogate(charSequence.charAt(i)) || i4 >= i2 || !isLowSurrogate(charSequence.charAt(i4))) {
                i = i4;
            } else {
                i3--;
                i = i4 + 1;
            }
        }
        return i3;
    }

    public static int codePointCount(char[] cArr, int i, int i2) {
        if (i2 <= cArr.length - i && i >= 0 && i2 >= 0) {
            return codePointCountImpl(cArr, i, i2);
        }
        throw new IndexOutOfBoundsException();
    }

    static int codePointCountImpl(char[] cArr, int i, int i2) {
        int i3 = i + i2;
        while (i < i3) {
            int i4 = i + 1;
            if (!isHighSurrogate(cArr[i]) || i4 >= i3 || !isLowSurrogate(cArr[i4])) {
                i = i4;
            } else {
                i2--;
                i = i4 + 1;
            }
        }
        return i2;
    }

    public static int offsetByCodePoints(CharSequence charSequence, int i, int i2) {
        int length = charSequence.length();
        if (i < 0 || i > length) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 >= 0) {
            int i3 = 0;
            while (i < length && i3 < i2) {
                int i4 = i + 1;
                if (isHighSurrogate(charSequence.charAt(i)) && i4 < length && isLowSurrogate(charSequence.charAt(i4))) {
                    i4++;
                }
                i = i4;
                i3++;
            }
            if (i3 < i2) {
                throw new IndexOutOfBoundsException();
            }
        } else {
            while (i > 0 && i2 < 0) {
                i--;
                if (isLowSurrogate(charSequence.charAt(i)) && i > 0 && isHighSurrogate(charSequence.charAt(i - 1))) {
                    i--;
                }
                i2++;
            }
            if (i2 < 0) {
                throw new IndexOutOfBoundsException();
            }
        }
        return i;
    }

    public static int offsetByCodePoints(char[] cArr, int i, int i2, int i3, int i4) {
        if (i2 <= cArr.length - i && i >= 0 && i2 >= 0 && i3 >= i && i3 <= i + i2) {
            return offsetByCodePointsImpl(cArr, i, i2, i3, i4);
        }
        throw new IndexOutOfBoundsException();
    }

    static int offsetByCodePointsImpl(char[] cArr, int i, int i2, int i3, int i4) {
        if (i4 >= 0) {
            int i5 = i + i2;
            int i6 = 0;
            while (i3 < i5 && i6 < i4) {
                int i7 = i3 + 1;
                if (isHighSurrogate(cArr[i3]) && i7 < i5 && isLowSurrogate(cArr[i7])) {
                    i7++;
                }
                i3 = i7;
                i6++;
            }
            if (i6 < i4) {
                throw new IndexOutOfBoundsException();
            }
        } else {
            while (i3 > i && i4 < 0) {
                i3--;
                if (isLowSurrogate(cArr[i3]) && i3 > i && isHighSurrogate(cArr[i3 - 1])) {
                    i3--;
                }
                i4++;
            }
            if (i4 < 0) {
                throw new IndexOutOfBoundsException();
            }
        }
        return i3;
    }

    public static boolean isLowerCase(char c) {
        return isLowerCase((int) c);
    }

    public static boolean isLowerCase(int i) {
        return isLowerCaseImpl(i);
    }

    public static boolean isUpperCase(char c) {
        return isUpperCase((int) c);
    }

    public static boolean isUpperCase(int i) {
        return isUpperCaseImpl(i);
    }

    public static boolean isTitleCase(char c) {
        return isTitleCase((int) c);
    }

    public static boolean isTitleCase(int i) {
        return isTitleCaseImpl(i);
    }

    public static boolean isDigit(char c) {
        return isDigit((int) c);
    }

    public static boolean isDigit(int i) {
        return isDigitImpl(i);
    }

    public static boolean isDefined(char c) {
        return isDefined((int) c);
    }

    public static boolean isDefined(int i) {
        return isDefinedImpl(i);
    }

    public static boolean isLetter(char c) {
        return isLetter((int) c);
    }

    public static boolean isLetter(int i) {
        return isLetterImpl(i);
    }

    public static boolean isLetterOrDigit(char c) {
        return isLetterOrDigit((int) c);
    }

    public static boolean isLetterOrDigit(int i) {
        return isLetterOrDigitImpl(i);
    }

    @Deprecated
    public static boolean isJavaLetter(char c) {
        return isJavaIdentifierStart(c);
    }

    @Deprecated
    public static boolean isJavaLetterOrDigit(char c) {
        return isJavaIdentifierPart(c);
    }

    public static boolean isAlphabetic(int i) {
        return isAlphabeticImpl(i);
    }

    public static boolean isIdeographic(int i) {
        return isIdeographicImpl(i);
    }

    public static boolean isJavaIdentifierStart(char c) {
        return isJavaIdentifierStart((int) c);
    }

    public static boolean isJavaIdentifierStart(int i) {
        return i < 64 ? i == 36 : i < 128 ? ((1 << (i - 64)) & 576460745995190270L) != 0 : ((1 << getType(i)) & 75498558) != 0;
    }

    public static boolean isJavaIdentifierPart(char c) {
        return isJavaIdentifierPart((int) c);
    }

    public static boolean isJavaIdentifierPart(int i) {
        return i < 64 ? ((1 << i) & 287948970162897407L) != 0 : i < 128 ? ((1 << (i - 64)) & -8646911290859585538L) != 0 : ((1 << getType(i)) & 75564926) != 0 || (i >= 0 && i <= 8) || ((i >= 14 && i <= 27) || (i >= 127 && i <= 159));
    }

    public static boolean isUnicodeIdentifierStart(char c) {
        return isUnicodeIdentifierStart((int) c);
    }

    public static boolean isUnicodeIdentifierStart(int i) {
        return isUnicodeIdentifierStartImpl(i);
    }

    public static boolean isUnicodeIdentifierPart(char c) {
        return isUnicodeIdentifierPart((int) c);
    }

    public static boolean isUnicodeIdentifierPart(int i) {
        return isUnicodeIdentifierPartImpl(i);
    }

    public static boolean isIdentifierIgnorable(char c) {
        return isIdentifierIgnorable((int) c);
    }

    public static boolean isIdentifierIgnorable(int i) {
        return isIdentifierIgnorableImpl(i);
    }

    public static char toLowerCase(char c) {
        return (char) toLowerCase((int) c);
    }

    public static int toLowerCase(int i) {
        if (i < 65 || i > 90) {
            return i < 128 ? i : toLowerCaseImpl(i);
        }
        return i + 32;
    }

    public static char toUpperCase(char c) {
        return (char) toUpperCase((int) c);
    }

    public static int toUpperCase(int i) {
        if (i < 97 || i > 122) {
            return i < 128 ? i : toUpperCaseImpl(i);
        }
        return i - 32;
    }

    public static char toTitleCase(char c) {
        return (char) toTitleCase((int) c);
    }

    public static int toTitleCase(int i) {
        return toTitleCaseImpl(i);
    }

    public static int digit(char c, int i) {
        return digit((int) c, i);
    }

    public static int digit(int i, int i2) {
        int i3;
        if (i2 < 2 || i2 > 36) {
            return -1;
        }
        if (i >= 128) {
            return digitImpl(i, i2);
        }
        if (48 > i || i > 57) {
            int i4 = 97;
            if (97 > i || i > 122) {
                i4 = 65;
                if (65 > i || i > 90) {
                    i3 = -1;
                }
            }
            i3 = (i - i4) + 10;
        } else {
            i3 = i - 48;
        }
        if (i3 < i2) {
            return i3;
        }
        return -1;
    }

    public static int getNumericValue(char c) {
        return getNumericValue((int) c);
    }

    public static int getNumericValue(int i) {
        if (i < 128) {
            if (i >= 48 && i <= 57) {
                return i - 48;
            }
            if (i >= 97 && i <= 122) {
                return i - 87;
            }
            if (i < 65 || i > 90) {
                return -1;
            }
            return i - 55;
        } else if (i < 65313 || i > 65338) {
            return (i < 65345 || i > 65370) ? getNumericValueImpl(i) : i - 65335;
        } else {
            return i - 65303;
        }
    }

    public static boolean isSpaceChar(char c) {
        return isSpaceChar((int) c);
    }

    public static boolean isSpaceChar(int i) {
        if (!(i == 32 || i == 160)) {
            if (i < 4096) {
                return false;
            }
            if (!(i == 5760 || i == 6158)) {
                if (i < 8192) {
                    return false;
                }
                if (i <= 65535) {
                    return i <= 8202 || i == 8232 || i == 8233 || i == 8239 || i == 8287 || i == 12288;
                }
                return isSpaceCharImpl(i);
            }
        }
        return true;
    }

    public static boolean isWhitespace(char c) {
        return isWhitespace((int) c);
    }

    public static boolean isWhitespace(int i) {
        if ((i >= 28 && i <= 32) || (i >= 9 && i <= 13)) {
            return true;
        }
        if (i < 4096) {
            return false;
        }
        if (i == 5760 || i == 6158) {
            return true;
        }
        if (i < 8192 || i == 8199 || i == 8239) {
            return false;
        }
        if (i <= 65535) {
            return i <= 8202 || i == 8232 || i == 8233 || i == 8287 || i == 12288;
        }
        return isWhitespaceImpl(i);
    }

    public static boolean isISOControl(char c) {
        return isISOControl((int) c);
    }

    public static int getType(char c) {
        return getType((int) c);
    }

    public static int getType(int i) {
        int typeImpl = getTypeImpl(i);
        return typeImpl <= 16 ? typeImpl : typeImpl + 1;
    }

    public static byte getDirectionality(char c) {
        return getDirectionality((int) c);
    }

    public static byte getDirectionality(int i) {
        byte directionalityImpl;
        if (getType(i) != 0 && (directionalityImpl = getDirectionalityImpl(i)) >= 0) {
            byte[] bArr = DIRECTIONALITY;
            if (directionalityImpl < bArr.length) {
                return bArr[directionalityImpl];
            }
        }
        return -1;
    }

    public static boolean isMirrored(char c) {
        return isMirrored((int) c);
    }

    public static boolean isMirrored(int i) {
        return isMirroredImpl(i);
    }

    public int compareTo(Character ch) {
        return compare(this.value, ch.value);
    }

    public static String getName(int i) {
        if (isValidCodePoint(i)) {
            String nameImpl = getNameImpl(i);
            if (nameImpl != null) {
                return nameImpl;
            }
            if (getType(i) == 0) {
                return null;
            }
            UnicodeBlock of = UnicodeBlock.m1695of(i);
            if (of == null) {
                return Integer.toHexString(i).toUpperCase(Locale.ENGLISH);
            }
            return of.toString().replace('_', ' ') + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + Integer.toHexString(i).toUpperCase(Locale.ENGLISH);
        }
        throw new IllegalArgumentException();
    }
}
