package com.android.settings.network.telephony;

import com.google.protobuf.Internal;

public enum NetworkModeChoicesProto$EnabledNetworks implements Internal.EnumLite {
    ENABLED_NETWORKS_UNSPECIFIED(0),
    ENABLED_NETWORKS_UNKNOWN(1),
    ENABLED_NETWORKS_CDMA_CHOICES(2),
    ENABLED_NETWORKS_CDMA_NO_LTE_CHOICES(3),
    ENABLED_NETWORKS_CDMA_ONLY_LTE_CHOICES(4),
    ENABLED_NETWORKS_TDSCDMA_CHOICES(5),
    ENABLED_NETWORKS_EXCEPT_GSM_LTE_CHOICES(6),
    ENABLED_NETWORKS_EXCEPT_GSM_4G_CHOICES(7),
    ENABLED_NETWORKS_EXCEPT_GSM_CHOICES(8),
    ENABLED_NETWORKS_EXCEPT_LTE_CHOICES(9),
    ENABLED_NETWORKS_4G_CHOICES(10),
    ENABLED_NETWORKS_CHOICES(11),
    PREFERRED_NETWORK_MODE_CHOICES_WORLD_MODE(12);
    
    private static final Internal.EnumLiteMap<NetworkModeChoicesProto$EnabledNetworks> internalValueMap = null;
    private final int value;

    static {
        internalValueMap = new Internal.EnumLiteMap<NetworkModeChoicesProto$EnabledNetworks>() {
            public NetworkModeChoicesProto$EnabledNetworks findValueByNumber(int i) {
                return NetworkModeChoicesProto$EnabledNetworks.forNumber(i);
            }
        };
    }

    public final int getNumber() {
        return this.value;
    }

    public static NetworkModeChoicesProto$EnabledNetworks forNumber(int i) {
        switch (i) {
            case 0:
                return ENABLED_NETWORKS_UNSPECIFIED;
            case 1:
                return ENABLED_NETWORKS_UNKNOWN;
            case 2:
                return ENABLED_NETWORKS_CDMA_CHOICES;
            case 3:
                return ENABLED_NETWORKS_CDMA_NO_LTE_CHOICES;
            case 4:
                return ENABLED_NETWORKS_CDMA_ONLY_LTE_CHOICES;
            case 5:
                return ENABLED_NETWORKS_TDSCDMA_CHOICES;
            case 6:
                return ENABLED_NETWORKS_EXCEPT_GSM_LTE_CHOICES;
            case 7:
                return ENABLED_NETWORKS_EXCEPT_GSM_4G_CHOICES;
            case 8:
                return ENABLED_NETWORKS_EXCEPT_GSM_CHOICES;
            case 9:
                return ENABLED_NETWORKS_EXCEPT_LTE_CHOICES;
            case 10:
                return ENABLED_NETWORKS_4G_CHOICES;
            case 11:
                return ENABLED_NETWORKS_CHOICES;
            case 12:
                return PREFERRED_NETWORK_MODE_CHOICES_WORLD_MODE;
            default:
                return null;
        }
    }

    public static Internal.EnumVerifier internalGetVerifier() {
        return EnabledNetworksVerifier.INSTANCE;
    }

    private static final class EnabledNetworksVerifier implements Internal.EnumVerifier {
        static final Internal.EnumVerifier INSTANCE = null;

        private EnabledNetworksVerifier() {
        }

        static {
            INSTANCE = new EnabledNetworksVerifier();
        }

        public boolean isInRange(int i) {
            return NetworkModeChoicesProto$EnabledNetworks.forNumber(i) != null;
        }
    }

    private NetworkModeChoicesProto$EnabledNetworks(int i) {
        this.value = i;
    }
}
