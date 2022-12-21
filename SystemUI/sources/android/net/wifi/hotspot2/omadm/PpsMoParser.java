package android.net.wifi.hotspot2.omadm;

import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.pps.Credential;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.net.wifi.hotspot2.pps.Policy;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.p026io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXException;

public final class PpsMoParser {
    private static final String NODE_AAA_SERVER_TRUSTED_NAMES = "AAAServerTrustedNames";
    private static final String NODE_AAA_SERVER_TRUST_ROOT = "AAAServerTrustRoot";
    private static final String NODE_ABLE_TO_SHARE = "AbleToShare";
    private static final String NODE_CERTIFICATE_TYPE = "CertificateType";
    private static final String NODE_CERT_SHA256_FINGERPRINT = "CertSHA256Fingerprint";
    private static final String NODE_CERT_URL = "CertURL";
    private static final String NODE_CHECK_AAA_SERVER_CERT_STATUS = "CheckAAAServerCertStatus";
    private static final String NODE_COUNTRY = "Country";
    private static final String NODE_CREATION_DATE = "CreationDate";
    private static final String NODE_CREDENTIAL = "Credential";
    private static final String NODE_CREDENTIAL_PRIORITY = "CredentialPriority";
    private static final String NODE_DATA_LIMIT = "DataLimit";
    private static final String NODE_DECORATED_IDENTITY_PREFIX = "DecoratedPrefix";
    private static final String NODE_DIGITAL_CERTIFICATE = "DigitalCertificate";
    private static final String NODE_DOWNLINK_BANDWIDTH = "DLBandwidth";
    private static final String NODE_EAP_METHOD = "EAPMethod";
    private static final String NODE_EAP_TYPE = "EAPType";
    private static final String NODE_EXPIRATION_DATE = "ExpirationDate";
    private static final String NODE_EXTENSION = "Extension";
    private static final String NODE_EXTENSION_NAI = "NAI";
    private static final String NODE_FQDN = "FQDN";
    private static final String NODE_FQDN_MATCH = "FQDN_Match";
    private static final String NODE_FRIENDLY_NAME = "FriendlyName";
    private static final String NODE_HESSID = "HESSID";
    private static final String NODE_HOMESP = "HomeSP";
    private static final String NODE_HOME_OI = "HomeOI";
    private static final String NODE_HOME_OI_LIST = "HomeOIList";
    private static final String NODE_HOME_OI_REQUIRED = "HomeOIRequired";
    private static final String NODE_ICON_URL = "IconURL";
    private static final String NODE_INNER_EAP_TYPE = "InnerEAPType";
    private static final String NODE_INNER_METHOD = "InnerMethod";
    private static final String NODE_INNER_VENDOR_ID = "InnerVendorID";
    private static final String NODE_INNER_VENDOR_TYPE = "InnerVendorType";
    private static final String NODE_IP_PROTOCOL = "IPProtocol";
    private static final String NODE_MACHINE_MANAGED = "MachineManaged";
    private static final String NODE_MAXIMUM_BSS_LOAD_VALUE = "MaximumBSSLoadValue";
    private static final String NODE_MIN_BACKHAUL_THRESHOLD = "MinBackhaulThreshold";
    private static final String NODE_NETWORK_ID = "NetworkID";
    private static final String NODE_NETWORK_TYPE = "NetworkType";
    private static final String NODE_OTHER = "Other";
    private static final String NODE_OTHER_HOME_PARTNERS = "OtherHomePartners";
    private static final String NODE_PASSWORD = "Password";
    private static final String NODE_PER_PROVIDER_SUBSCRIPTION = "PerProviderSubscription";
    private static final String NODE_POLICY = "Policy";
    private static final String NODE_POLICY_UPDATE = "PolicyUpdate";
    private static final String NODE_PORT_NUMBER = "PortNumber";
    private static final String NODE_PREFERRED_ROAMING_PARTNER_LIST = "PreferredRoamingPartnerList";
    private static final String NODE_PRIORITY = "Priority";
    private static final String NODE_REALM = "Realm";
    private static final String NODE_REQUIRED_PROTO_PORT_TUPLE = "RequiredProtoPortTuple";
    private static final String NODE_RESTRICTION = "Restriction";
    private static final String NODE_ROAMING_CONSORTIUM_OI = "RoamingConsortiumOI";
    private static final String NODE_SIM = "SIM";
    private static final String NODE_SIM_IMSI = "IMSI";
    private static final String NODE_SOFT_TOKEN_APP = "SoftTokenApp";
    private static final String NODE_SP_EXCLUSION_LIST = "SPExclusionList";
    private static final String NODE_SSID = "SSID";
    private static final String NODE_START_DATE = "StartDate";
    private static final String NODE_SUBSCRIPTION_PARAMETER = "SubscriptionParameters";
    private static final String NODE_SUBSCRIPTION_UPDATE = "SubscriptionUpdate";
    private static final String NODE_TIME_LIMIT = "TimeLimit";
    private static final String NODE_TRUST_ROOT = "TrustRoot";
    private static final String NODE_TYPE_OF_SUBSCRIPTION = "TypeOfSubscription";
    private static final String NODE_UPDATE_IDENTIFIER = "UpdateIdentifier";
    private static final String NODE_UPDATE_INTERVAL = "UpdateInterval";
    private static final String NODE_UPDATE_METHOD = "UpdateMethod";
    private static final String NODE_UPLINK_BANDWIDTH = "ULBandwidth";
    private static final String NODE_URI = "URI";
    private static final String NODE_USAGE_LIMITS = "UsageLimits";
    private static final String NODE_USAGE_TIME_PERIOD = "UsageTimePeriod";
    private static final String NODE_USERNAME = "Username";
    private static final String NODE_USERNAME_PASSWORD = "UsernamePassword";
    private static final String NODE_VENDOR_ANDROID = "Android";
    private static final String NODE_VENDOR_ID = "VendorId";
    private static final String NODE_VENDOR_TYPE = "VendorType";
    private static final String NODE_VENDOR_WBA = "WBA";
    private static final String PPS_MO_URN = "urn:wfa:mo:hotspot2dot0-perprovidersubscription:1.0";
    private static final String TAG = "PpsMoParser";
    private static final String TAG_DDF_NAME = "DDFName";
    private static final String TAG_MANAGEMENT_TREE = "MgmtTree";
    private static final String TAG_NODE = "Node";
    private static final String TAG_NODE_NAME = "NodeName";
    private static final String TAG_RT_PROPERTIES = "RTProperties";
    private static final String TAG_TYPE = "Type";
    private static final String TAG_VALUE = "Value";
    private static final String TAG_VER_DTD = "VerDTD";

    private static class ParsingException extends Exception {
        public ParsingException(String str) {
            super(str);
        }
    }

    private static abstract class PPSNode {
        private final String mName;

        public abstract List<PPSNode> getChildren();

        public abstract String getValue();

        public abstract boolean isLeaf();

        public PPSNode(String str) {
            this.mName = str;
        }

        public String getName() {
            return this.mName;
        }
    }

    private static class LeafNode extends PPSNode {
        private final String mValue;

        public List<PPSNode> getChildren() {
            return null;
        }

        public boolean isLeaf() {
            return true;
        }

        public LeafNode(String str, String str2) {
            super(str);
            this.mValue = str2;
        }

        public String getValue() {
            return this.mValue;
        }
    }

    private static class InternalNode extends PPSNode {
        private final List<PPSNode> mChildren;

        public String getValue() {
            return null;
        }

        public boolean isLeaf() {
            return false;
        }

        public InternalNode(String str, List<PPSNode> list) {
            super(str);
            this.mChildren = list;
        }

        public List<PPSNode> getChildren() {
            return this.mChildren;
        }
    }

    public static PasspointConfiguration parseMoText(String str) {
        try {
            XMLNode parse = new XMLParser().parse(str);
            if (parse == null) {
                Log.e(TAG, "Root is not available");
                return null;
            } else if (parse.getTag() != TAG_MANAGEMENT_TREE) {
                Log.e(TAG, "Root is not a MgmtTree");
                return null;
            } else {
                PasspointConfiguration passpointConfiguration = null;
                String str2 = null;
                for (XMLNode next : parse.getChildren()) {
                    String tag = next.getTag();
                    tag.hashCode();
                    if (!tag.equals(TAG_VER_DTD)) {
                        if (!tag.equals(TAG_NODE)) {
                            Log.e(TAG, "Unknown node: " + next.getTag());
                            return null;
                        } else if (passpointConfiguration != null) {
                            Log.e(TAG, "Unexpected multiple Node element under MgmtTree");
                            return null;
                        } else {
                            try {
                                passpointConfiguration = parsePpsNode(next);
                            } catch (ParsingException e) {
                                Log.e(TAG, e.getMessage());
                                return null;
                            }
                        }
                    } else if (str2 != null) {
                        Log.e(TAG, "Duplicate VerDTD element");
                        return null;
                    } else {
                        str2 = next.getText();
                    }
                }
                return passpointConfiguration;
            }
        } catch (IOException | SAXException unused) {
            Log.e(TAG, "Failed to parse XML input");
            return null;
        }
    }

    private static PasspointConfiguration parsePpsNode(XMLNode xMLNode) throws ParsingException {
        PasspointConfiguration passpointConfiguration = null;
        String str = null;
        int i = Integer.MIN_VALUE;
        for (XMLNode next : xMLNode.getChildren()) {
            String tag = next.getTag();
            tag.hashCode();
            char c = 65535;
            switch (tag.hashCode()) {
                case -1852765931:
                    if (tag.equals(TAG_RT_PROPERTIES)) {
                        c = 0;
                        break;
                    }
                    break;
                case 2433570:
                    if (tag.equals(TAG_NODE)) {
                        c = 1;
                        break;
                    }
                    break;
                case 1187524557:
                    if (tag.equals(TAG_NODE_NAME)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    String parseUrn = parseUrn(next);
                    if (TextUtils.equals(parseUrn, PPS_MO_URN)) {
                        break;
                    } else {
                        throw new ParsingException("Unknown URN: " + parseUrn);
                    }
                case 1:
                    PPSNode buildPpsNode = buildPpsNode(next);
                    if (TextUtils.equals(buildPpsNode.getName(), NODE_UPDATE_IDENTIFIER)) {
                        if (i == Integer.MIN_VALUE) {
                            i = parseInteger(getPpsNodeValue(buildPpsNode));
                            break;
                        } else {
                            throw new ParsingException("Multiple node for UpdateIdentifier");
                        }
                    } else if (passpointConfiguration == null) {
                        passpointConfiguration = parsePpsInstance(buildPpsNode);
                        break;
                    } else {
                        throw new ParsingException("Multiple PPS instance");
                    }
                case 2:
                    if (str == null) {
                        str = next.getText();
                        if (TextUtils.equals(str, NODE_PER_PROVIDER_SUBSCRIPTION)) {
                            break;
                        } else {
                            throw new ParsingException("Unexpected NodeName: " + str);
                        }
                    } else {
                        throw new ParsingException("Duplicate NodeName: " + next.getText());
                    }
                default:
                    throw new ParsingException("Unknown tag under PPS node: " + next.getTag());
            }
        }
        if (!(passpointConfiguration == null || i == Integer.MIN_VALUE)) {
            passpointConfiguration.setUpdateIdentifier(i);
        }
        return passpointConfiguration;
    }

    private static String parseUrn(XMLNode xMLNode) throws ParsingException {
        if (xMLNode.getChildren().size() == 1) {
            XMLNode xMLNode2 = xMLNode.getChildren().get(0);
            if (xMLNode2.getChildren().size() != 1) {
                throw new ParsingException("Expect Type node to only have one child");
            } else if (TextUtils.equals(xMLNode2.getTag(), TAG_TYPE)) {
                XMLNode xMLNode3 = xMLNode2.getChildren().get(0);
                if (!xMLNode3.getChildren().isEmpty()) {
                    throw new ParsingException("Expect DDFName node to have no child");
                } else if (TextUtils.equals(xMLNode3.getTag(), TAG_DDF_NAME)) {
                    return xMLNode3.getText();
                } else {
                    throw new ParsingException("Unexpected tag for DDFName: " + xMLNode3.getTag());
                }
            } else {
                throw new ParsingException("Unexpected tag for Type: " + xMLNode2.getTag());
            }
        } else {
            throw new ParsingException("Expect RTPProperties node to only have one child");
        }
    }

    private static PPSNode buildPpsNode(XMLNode xMLNode) throws ParsingException {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        String str = null;
        String str2 = null;
        for (XMLNode next : xMLNode.getChildren()) {
            String tag = next.getTag();
            if (TextUtils.equals(tag, TAG_NODE_NAME)) {
                if (str == null) {
                    str = next.getText();
                } else {
                    throw new ParsingException("Duplicate NodeName node");
                }
            } else if (TextUtils.equals(tag, TAG_NODE)) {
                PPSNode buildPpsNode = buildPpsNode(next);
                if (!hashSet.contains(buildPpsNode.getName())) {
                    hashSet.add(buildPpsNode.getName());
                    arrayList.add(buildPpsNode);
                } else {
                    throw new ParsingException("Duplicate node: " + buildPpsNode.getName());
                }
            } else if (!TextUtils.equals(tag, TAG_VALUE)) {
                throw new ParsingException("Unknown tag: " + tag);
            } else if (str2 == null) {
                str2 = next.getText();
            } else {
                throw new ParsingException("Duplicate Value node");
            }
        }
        if (str == null) {
            throw new ParsingException("Invalid node: missing NodeName");
        } else if (str2 == null && arrayList.size() == 0) {
            throw new ParsingException("Invalid node: " + str + " missing both value and children");
        } else if (str2 != null && arrayList.size() > 0) {
            throw new ParsingException("Invalid node: " + str + " contained both value and children");
        } else if (str2 != null) {
            return new LeafNode(str, str2);
        } else {
            return new InternalNode(str, arrayList);
        }
    }

    private static String getPpsNodeValue(PPSNode pPSNode) throws ParsingException {
        if (pPSNode.isLeaf()) {
            return pPSNode.getValue();
        }
        throw new ParsingException("Cannot get value from a non-leaf node: " + pPSNode.getName());
    }

    private static PasspointConfiguration parsePpsInstance(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            PasspointConfiguration passpointConfiguration = new PasspointConfiguration();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -2127810660:
                        if (name.equals("HomeSP")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1898802862:
                        if (name.equals(NODE_POLICY)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 162345062:
                        if (name.equals(NODE_SUBSCRIPTION_UPDATE)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 314411254:
                        if (name.equals(NODE_AAA_SERVER_TRUST_ROOT)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1112908551:
                        if (name.equals(NODE_SUBSCRIPTION_PARAMETER)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 1310049399:
                        if (name.equals(NODE_CREDENTIAL)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 1391410207:
                        if (name.equals(NODE_EXTENSION)) {
                            c = 6;
                            break;
                        }
                        break;
                    case 2017737531:
                        if (name.equals(NODE_CREDENTIAL_PRIORITY)) {
                            c = 7;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        passpointConfiguration.setHomeSp(parseHomeSP(next));
                        break;
                    case 1:
                        passpointConfiguration.setPolicy(parsePolicy(next));
                        break;
                    case 2:
                        passpointConfiguration.setSubscriptionUpdate(parseUpdateParameter(next));
                        break;
                    case 3:
                        passpointConfiguration.setTrustRootCertList(parseAAAServerTrustRootList(next));
                        break;
                    case 4:
                        parseSubscriptionParameter(next, passpointConfiguration);
                        break;
                    case 5:
                        passpointConfiguration.setCredential(parseCredential(next));
                        break;
                    case 6:
                        parseExtension(next, passpointConfiguration);
                        break;
                    case 7:
                        passpointConfiguration.setCredentialPriority(parseInteger(getPpsNodeValue(next)));
                        break;
                    default:
                        throw new ParsingException("Unknown node: " + next.getName());
                }
            }
            return passpointConfiguration;
        }
        throw new ParsingException("Leaf node not expected for PPS instance");
    }

    private static HomeSp parseHomeSP(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            HomeSp homeSp = new HomeSp();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1560207529:
                        if (name.equals(NODE_HOME_OI_LIST)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -991549930:
                        if (name.equals(NODE_ICON_URL)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -228216919:
                        if (name.equals(NODE_NETWORK_ID)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 2165397:
                        if (name.equals(NODE_FQDN)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 542998228:
                        if (name.equals(NODE_ROAMING_CONSORTIUM_OI)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 626253302:
                        if (name.equals(NODE_FRIENDLY_NAME)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 1956561338:
                        if (name.equals(NODE_OTHER_HOME_PARTNERS)) {
                            c = 6;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        Pair<List<Long>, List<Long>> parseHomeOIList = parseHomeOIList(next);
                        homeSp.setMatchAllOis(convertFromLongList((List) parseHomeOIList.first));
                        homeSp.setMatchAnyOis(convertFromLongList((List) parseHomeOIList.second));
                        break;
                    case 1:
                        homeSp.setIconUrl(getPpsNodeValue(next));
                        break;
                    case 2:
                        homeSp.setHomeNetworkIds(parseNetworkIds(next));
                        break;
                    case 3:
                        homeSp.setFqdn(getPpsNodeValue(next));
                        break;
                    case 4:
                        homeSp.setRoamingConsortiumOis(parseRoamingConsortiumOI(getPpsNodeValue(next)));
                        break;
                    case 5:
                        homeSp.setFriendlyName(getPpsNodeValue(next));
                        break;
                    case 6:
                        homeSp.setOtherHomePartners(parseOtherHomePartners(next));
                        break;
                    default:
                        throw new ParsingException("Unknown node under HomeSP: " + next.getName());
                }
            }
            return homeSp;
        }
        throw new ParsingException("Leaf node not expected for HomeSP");
    }

    private static long[] parseRoamingConsortiumOI(String str) throws ParsingException {
        String[] split = str.split(NavigationBarInflaterView.BUTTON_SEPARATOR);
        long[] jArr = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            jArr[i] = parseLong(split[i], 16);
        }
        return jArr;
    }

    private static Map<String, Long> parseNetworkIds(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            HashMap hashMap = new HashMap();
            for (PPSNode parseNetworkIdInstance : pPSNode.getChildren()) {
                Pair<String, Long> parseNetworkIdInstance2 = parseNetworkIdInstance(parseNetworkIdInstance);
                hashMap.put((String) parseNetworkIdInstance2.first, (Long) parseNetworkIdInstance2.second);
            }
            return hashMap;
        }
        throw new ParsingException("Leaf node not expected for NetworkID");
    }

    private static Pair<String, Long> parseNetworkIdInstance(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            String str = null;
            Long l = null;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_SSID)) {
                    str = getPpsNodeValue(next);
                } else if (name.equals(NODE_HESSID)) {
                    l = Long.valueOf(parseLong(getPpsNodeValue(next), 16));
                } else {
                    throw new ParsingException("Unknown node under NetworkID instance: " + next.getName());
                }
            }
            if (str != null) {
                return new Pair<>(str, l);
            }
            throw new ParsingException("NetworkID instance missing SSID");
        }
        throw new ParsingException("Leaf node not expected for NetworkID instance");
    }

    private static Pair<List<Long>, List<Long>> parseHomeOIList(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (PPSNode parseHomeOIInstance : pPSNode.getChildren()) {
                Pair<Long, Boolean> parseHomeOIInstance2 = parseHomeOIInstance(parseHomeOIInstance);
                if (((Boolean) parseHomeOIInstance2.second).booleanValue()) {
                    arrayList.add((Long) parseHomeOIInstance2.first);
                } else {
                    arrayList2.add((Long) parseHomeOIInstance2.first);
                }
            }
            return new Pair<>(arrayList, arrayList2);
        }
        throw new ParsingException("Leaf node not expected for HomeOIList");
    }

    private static Pair<Long, Boolean> parseHomeOIInstance(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            Long l = null;
            Boolean bool = null;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_HOME_OI)) {
                    try {
                        l = Long.valueOf(getPpsNodeValue(next), 16);
                    } catch (NumberFormatException unused) {
                        throw new ParsingException("Invalid HomeOI: " + getPpsNodeValue(next));
                    }
                } else if (name.equals(NODE_HOME_OI_REQUIRED)) {
                    bool = Boolean.valueOf(getPpsNodeValue(next));
                } else {
                    throw new ParsingException("Unknown node under NetworkID instance: " + next.getName());
                }
            }
            if (l == null) {
                throw new ParsingException("HomeOI instance missing OI field");
            } else if (bool != null) {
                return new Pair<>(l, bool);
            } else {
                throw new ParsingException("HomeOI instance missing required field");
            }
        } else {
            throw new ParsingException("Leaf node not expected for HomeOI instance");
        }
    }

    private static String[] parseOtherHomePartners(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            ArrayList arrayList = new ArrayList();
            for (PPSNode parseOtherHomePartnerInstance : pPSNode.getChildren()) {
                arrayList.add(parseOtherHomePartnerInstance(parseOtherHomePartnerInstance));
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        throw new ParsingException("Leaf node not expected for OtherHomePartners");
    }

    private static String parseOtherHomePartnerInstance(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            String str = null;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_FQDN)) {
                    str = getPpsNodeValue(next);
                } else {
                    throw new ParsingException("Unknown node under OtherHomePartner instance: " + next.getName());
                }
            }
            if (str != null) {
                return str;
            }
            throw new ParsingException("OtherHomePartner instance missing FQDN field");
        }
        throw new ParsingException("Leaf node not expected for OtherHomePartner instance");
    }

    private static Credential parseCredential(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            Credential credential = new Credential();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1670804707:
                        if (name.equals(NODE_EXPIRATION_DATE)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1208321921:
                        if (name.equals(NODE_DIGITAL_CERTIFICATE)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 82103:
                        if (name.equals(NODE_SIM)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 78834287:
                        if (name.equals(NODE_REALM)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 494843313:
                        if (name.equals(NODE_USERNAME_PASSWORD)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 646045490:
                        if (name.equals(NODE_CHECK_AAA_SERVER_CERT_STATUS)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 1749851981:
                        if (name.equals(NODE_CREATION_DATE)) {
                            c = 6;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        credential.setExpirationTimeInMillis(parseDate(getPpsNodeValue(next)));
                        break;
                    case 1:
                        credential.setCertCredential(parseCertificateCredential(next));
                        break;
                    case 2:
                        credential.setSimCredential(parseSimCredential(next));
                        break;
                    case 3:
                        credential.setRealm(getPpsNodeValue(next));
                        break;
                    case 4:
                        credential.setUserCredential(parseUserCredential(next));
                        break;
                    case 5:
                        credential.setCheckAaaServerCertStatus(Boolean.parseBoolean(getPpsNodeValue(next)));
                        break;
                    case 6:
                        credential.setCreationTimeInMillis(parseDate(getPpsNodeValue(next)));
                        break;
                    default:
                        throw new ParsingException("Unknown node under Credential: " + next.getName());
                }
            }
            return credential;
        }
        throw new ParsingException("Leaf node not expected for Credential");
    }

    private static Credential.UserCredential parseUserCredential(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            Credential.UserCredential userCredential = new Credential.UserCredential();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -201069322:
                        if (name.equals(NODE_USERNAME)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -123996342:
                        if (name.equals(NODE_ABLE_TO_SHARE)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1045832056:
                        if (name.equals(NODE_MACHINE_MANAGED)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1281629883:
                        if (name.equals(NODE_PASSWORD)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1410776018:
                        if (name.equals(NODE_SOFT_TOKEN_APP)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 1740345653:
                        if (name.equals(NODE_EAP_METHOD)) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        userCredential.setUsername(getPpsNodeValue(next));
                        break;
                    case 1:
                        userCredential.setAbleToShare(Boolean.parseBoolean(getPpsNodeValue(next)));
                        break;
                    case 2:
                        userCredential.setMachineManaged(Boolean.parseBoolean(getPpsNodeValue(next)));
                        break;
                    case 3:
                        userCredential.setPassword(getPpsNodeValue(next));
                        break;
                    case 4:
                        userCredential.setSoftTokenApp(getPpsNodeValue(next));
                        break;
                    case 5:
                        parseEAPMethod(next, userCredential);
                        break;
                    default:
                        throw new ParsingException("Unknown node under UsernamePassword: " + next.getName());
                }
            }
            return userCredential;
        }
        throw new ParsingException("Leaf node not expected for UsernamePassword");
    }

    private static void parseEAPMethod(PPSNode pPSNode, Credential.UserCredential userCredential) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -2048597853:
                        if (name.equals(NODE_VENDOR_ID)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1706447464:
                        if (name.equals(NODE_INNER_EAP_TYPE)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1607163710:
                        if (name.equals(NODE_VENDOR_TYPE)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -1249356658:
                        if (name.equals(NODE_EAP_TYPE)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 541930360:
                        if (name.equals(NODE_INNER_VENDOR_TYPE)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 901061303:
                        if (name.equals(NODE_INNER_METHOD)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 961456313:
                        if (name.equals(NODE_INNER_VENDOR_ID)) {
                            c = 6;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                    case 6:
                        Log.d(TAG, "Ignore unsupported EAP method parameter: " + next.getName());
                        break;
                    case 3:
                        userCredential.setEapType(parseInteger(getPpsNodeValue(next)));
                        break;
                    case 5:
                        userCredential.setNonEapInnerMethod(getPpsNodeValue(next));
                        break;
                    default:
                        throw new ParsingException("Unknown node under EAPMethod: " + next.getName());
                }
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for EAPMethod");
    }

    private static Credential.CertificateCredential parseCertificateCredential(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            Credential.CertificateCredential certificateCredential = new Credential.CertificateCredential();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_CERTIFICATE_TYPE)) {
                    certificateCredential.setCertType(getPpsNodeValue(next));
                } else if (name.equals(NODE_CERT_SHA256_FINGERPRINT)) {
                    certificateCredential.setCertSha256Fingerprint(parseHexString(getPpsNodeValue(next)));
                } else {
                    throw new ParsingException("Unknown node under CertificateCredential: " + next.getName());
                }
            }
            return certificateCredential;
        }
        throw new ParsingException("Leaf node not expected for CertificateCredential");
    }

    private static Credential.SimCredential parseSimCredential(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            Credential.SimCredential simCredential = new Credential.SimCredential();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_EAP_TYPE)) {
                    simCredential.setEapType(parseInteger(getPpsNodeValue(next)));
                } else if (name.equals(NODE_SIM_IMSI)) {
                    simCredential.setImsi(getPpsNodeValue(next));
                } else {
                    throw new ParsingException("Unknown node under SimCredential: " + next.getName());
                }
            }
            return simCredential;
        }
        throw new ParsingException("Leaf node not expected for SimCredential");
    }

    private static Policy parsePolicy(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            Policy policy = new Policy();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1710886725:
                        if (name.equals(NODE_POLICY_UPDATE)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -281271454:
                        if (name.equals(NODE_MIN_BACKHAUL_THRESHOLD)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -166875607:
                        if (name.equals(NODE_MAXIMUM_BSS_LOAD_VALUE)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 586018863:
                        if (name.equals(NODE_SP_EXCLUSION_LIST)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 783647838:
                        if (name.equals(NODE_REQUIRED_PROTO_PORT_TUPLE)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 1337803246:
                        if (name.equals(NODE_PREFERRED_ROAMING_PARTNER_LIST)) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        policy.setPolicyUpdate(parseUpdateParameter(next));
                        break;
                    case 1:
                        parseMinBackhaulThreshold(next, policy);
                        break;
                    case 2:
                        policy.setMaximumBssLoadValue(parseInteger(getPpsNodeValue(next)));
                        break;
                    case 3:
                        policy.setExcludedSsidList(parseSpExclusionList(next));
                        break;
                    case 4:
                        policy.setRequiredProtoPortMap(parseRequiredProtoPortTuple(next));
                        break;
                    case 5:
                        policy.setPreferredRoamingPartnerList(parsePreferredRoamingPartnerList(next));
                        break;
                    default:
                        throw new ParsingException("Unknown node under Policy: " + next.getName());
                }
            }
            return policy;
        }
        throw new ParsingException("Leaf node not expected for Policy");
    }

    private static List<Policy.RoamingPartner> parsePreferredRoamingPartnerList(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            ArrayList arrayList = new ArrayList();
            for (PPSNode parsePreferredRoamingPartner : pPSNode.getChildren()) {
                arrayList.add(parsePreferredRoamingPartner(parsePreferredRoamingPartner));
            }
            return arrayList;
        }
        throw new ParsingException("Leaf node not expected for PreferredRoamingPartnerList");
    }

    private static Policy.RoamingPartner parsePreferredRoamingPartner(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            Policy.RoamingPartner roamingPartner = new Policy.RoamingPartner();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1672482954:
                        if (name.equals(NODE_COUNTRY)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1100816956:
                        if (name.equals(NODE_PRIORITY)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 305746811:
                        if (name.equals(NODE_FQDN_MATCH)) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        roamingPartner.setCountries(getPpsNodeValue(next));
                        break;
                    case 1:
                        roamingPartner.setPriority(parseInteger(getPpsNodeValue(next)));
                        break;
                    case 2:
                        String ppsNodeValue = getPpsNodeValue(next);
                        String[] split = ppsNodeValue.split(NavigationBarInflaterView.BUTTON_SEPARATOR);
                        if (split.length == 2) {
                            roamingPartner.setFqdn(split[0]);
                            if (TextUtils.equals(split[1], "exactMatch")) {
                                roamingPartner.setFqdnExactMatch(true);
                                break;
                            } else if (TextUtils.equals(split[1], "includeSubdomains")) {
                                roamingPartner.setFqdnExactMatch(false);
                                break;
                            } else {
                                throw new ParsingException("Invalid FQDN_Match: " + ppsNodeValue);
                            }
                        } else {
                            throw new ParsingException("Invalid FQDN_Match: " + ppsNodeValue);
                        }
                    default:
                        throw new ParsingException("Unknown node under PreferredRoamingPartnerList instance " + next.getName());
                }
            }
            return roamingPartner;
        }
        throw new ParsingException("Leaf node not expected for PreferredRoamingPartner instance");
    }

    private static void parseMinBackhaulThreshold(PPSNode pPSNode, Policy policy) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode parseMinBackhaulThresholdInstance : pPSNode.getChildren()) {
                parseMinBackhaulThresholdInstance(parseMinBackhaulThresholdInstance, policy);
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for MinBackhaulThreshold");
    }

    private static void parseMinBackhaulThresholdInstance(PPSNode pPSNode, Policy policy) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            long j = Long.MIN_VALUE;
            String str = null;
            long j2 = Long.MIN_VALUE;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -272744856:
                        if (name.equals(NODE_NETWORK_TYPE)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -133967910:
                        if (name.equals(NODE_UPLINK_BANDWIDTH)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 349434121:
                        if (name.equals(NODE_DOWNLINK_BANDWIDTH)) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        str = getPpsNodeValue(next);
                        break;
                    case 1:
                        j2 = parseLong(getPpsNodeValue(next), 10);
                        break;
                    case 2:
                        j = parseLong(getPpsNodeValue(next), 10);
                        break;
                    default:
                        throw new ParsingException("Unknown node under MinBackhaulThreshold instance " + next.getName());
                }
            }
            if (str == null) {
                throw new ParsingException("Missing NetworkType field");
            } else if (TextUtils.equals(str, NavigationBarInflaterView.HOME)) {
                policy.setMinHomeDownlinkBandwidth(j);
                policy.setMinHomeUplinkBandwidth(j2);
            } else if (TextUtils.equals(str, "roaming")) {
                policy.setMinRoamingDownlinkBandwidth(j);
                policy.setMinRoamingUplinkBandwidth(j2);
            } else {
                throw new ParsingException("Invalid network type: " + str);
            }
        } else {
            throw new ParsingException("Leaf node not expected for MinBackhaulThreshold instance");
        }
    }

    private static UpdateParameter parseUpdateParameter(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            UpdateParameter updateParameter = new UpdateParameter();
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -961491158:
                        if (name.equals(NODE_UPDATE_METHOD)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -524654790:
                        if (name.equals(NODE_TRUST_ROOT)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 84300:
                        if (name.equals(NODE_URI)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 76517104:
                        if (name.equals(NODE_OTHER)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 106806188:
                        if (name.equals(NODE_RESTRICTION)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 438596814:
                        if (name.equals(NODE_UPDATE_INTERVAL)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 494843313:
                        if (name.equals(NODE_USERNAME_PASSWORD)) {
                            c = 6;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        updateParameter.setUpdateMethod(getPpsNodeValue(next));
                        break;
                    case 1:
                        Pair<String, byte[]> parseTrustRoot = parseTrustRoot(next);
                        updateParameter.setTrustRootCertUrl((String) parseTrustRoot.first);
                        updateParameter.setTrustRootCertSha256Fingerprint((byte[]) parseTrustRoot.second);
                        break;
                    case 2:
                        updateParameter.setServerUri(getPpsNodeValue(next));
                        break;
                    case 3:
                        Log.d(TAG, "Ignore unsupported paramter: " + next.getName());
                        break;
                    case 4:
                        updateParameter.setRestriction(getPpsNodeValue(next));
                        break;
                    case 5:
                        updateParameter.setUpdateIntervalInMinutes(parseLong(getPpsNodeValue(next), 10));
                        break;
                    case 6:
                        Pair<String, String> parseUpdateUserCredential = parseUpdateUserCredential(next);
                        updateParameter.setUsername((String) parseUpdateUserCredential.first);
                        updateParameter.setBase64EncodedPassword((String) parseUpdateUserCredential.second);
                        break;
                    default:
                        throw new ParsingException("Unknown node under Update Parameters: " + next.getName());
                }
            }
            return updateParameter;
        }
        throw new ParsingException("Leaf node not expected for Update Parameters");
    }

    private static Pair<String, String> parseUpdateUserCredential(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            String str = null;
            String str2 = null;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_USERNAME)) {
                    str = getPpsNodeValue(next);
                } else if (name.equals(NODE_PASSWORD)) {
                    str2 = getPpsNodeValue(next);
                } else {
                    throw new ParsingException("Unknown node under UsernamePassword: " + next.getName());
                }
            }
            return Pair.create(str, str2);
        }
        throw new ParsingException("Leaf node not expected for UsernamePassword");
    }

    private static Pair<String, byte[]> parseTrustRoot(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            String str = null;
            byte[] bArr = null;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_CERT_URL)) {
                    str = getPpsNodeValue(next);
                } else if (name.equals(NODE_CERT_SHA256_FINGERPRINT)) {
                    bArr = parseHexString(getPpsNodeValue(next));
                } else {
                    throw new ParsingException("Unknown node under TrustRoot: " + next.getName());
                }
            }
            return Pair.create(str, bArr);
        }
        throw new ParsingException("Leaf node not expected for TrustRoot");
    }

    private static String[] parseSpExclusionList(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            ArrayList arrayList = new ArrayList();
            for (PPSNode parseSpExclusionInstance : pPSNode.getChildren()) {
                arrayList.add(parseSpExclusionInstance(parseSpExclusionInstance));
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        throw new ParsingException("Leaf node not expected for SPExclusionList");
    }

    private static String parseSpExclusionInstance(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            String str = null;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_SSID)) {
                    str = getPpsNodeValue(next);
                } else {
                    throw new ParsingException("Unknown node under SPExclusion instance");
                }
            }
            return str;
        }
        throw new ParsingException("Leaf node not expected for SPExclusion instance");
    }

    private static Map<Integer, String> parseRequiredProtoPortTuple(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            HashMap hashMap = new HashMap();
            for (PPSNode parseProtoPortTuple : pPSNode.getChildren()) {
                Pair<Integer, String> parseProtoPortTuple2 = parseProtoPortTuple(parseProtoPortTuple);
                hashMap.put((Integer) parseProtoPortTuple2.first, (String) parseProtoPortTuple2.second);
            }
            return hashMap;
        }
        throw new ParsingException("Leaf node not expected for RequiredProtoPortTuple");
    }

    private static Pair<Integer, String> parseProtoPortTuple(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            String str = null;
            int i = Integer.MIN_VALUE;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_IP_PROTOCOL)) {
                    i = parseInteger(getPpsNodeValue(next));
                } else if (name.equals(NODE_PORT_NUMBER)) {
                    str = getPpsNodeValue(next);
                } else {
                    throw new ParsingException("Unknown node under RequiredProtoPortTuple instance" + next.getName());
                }
            }
            if (i == Integer.MIN_VALUE) {
                throw new ParsingException("Missing IPProtocol field");
            } else if (str != null) {
                return Pair.create(Integer.valueOf(i), str);
            } else {
                throw new ParsingException("Missing PortNumber field");
            }
        } else {
            throw new ParsingException("Leaf node not expected for RequiredProtoPortTuple instance");
        }
    }

    private static Map<String, byte[]> parseAAAServerTrustRootList(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            HashMap hashMap = new HashMap();
            for (PPSNode parseTrustRoot : pPSNode.getChildren()) {
                Pair<String, byte[]> parseTrustRoot2 = parseTrustRoot(parseTrustRoot);
                hashMap.put((String) parseTrustRoot2.first, (byte[]) parseTrustRoot2.second);
            }
            return hashMap;
        }
        throw new ParsingException("Leaf node not expected for AAAServerTrustRoot");
    }

    private static void parseSubscriptionParameter(PPSNode pPSNode, PasspointConfiguration passpointConfiguration) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1930116871:
                        if (name.equals(NODE_USAGE_LIMITS)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1670804707:
                        if (name.equals(NODE_EXPIRATION_DATE)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1655596402:
                        if (name.equals(NODE_TYPE_OF_SUBSCRIPTION)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1749851981:
                        if (name.equals(NODE_CREATION_DATE)) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        parseUsageLimits(next, passpointConfiguration);
                        break;
                    case 1:
                        passpointConfiguration.setSubscriptionExpirationTimeInMillis(parseDate(getPpsNodeValue(next)));
                        break;
                    case 2:
                        passpointConfiguration.setSubscriptionType(getPpsNodeValue(next));
                        break;
                    case 3:
                        passpointConfiguration.setSubscriptionCreationTimeInMillis(parseDate(getPpsNodeValue(next)));
                        break;
                    default:
                        throw new ParsingException("Unknown node under SubscriptionParameter" + next.getName());
                }
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for SubscriptionParameter");
    }

    private static void parseUsageLimits(PPSNode pPSNode, PasspointConfiguration passpointConfiguration) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -125810928:
                        if (name.equals(NODE_START_DATE)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 587064143:
                        if (name.equals(NODE_USAGE_TIME_PERIOD)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1622722065:
                        if (name.equals(NODE_DATA_LIMIT)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 2022760654:
                        if (name.equals(NODE_TIME_LIMIT)) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        passpointConfiguration.setUsageLimitStartTimeInMillis(parseDate(getPpsNodeValue(next)));
                        break;
                    case 1:
                        passpointConfiguration.setUsageLimitUsageTimePeriodInMinutes(parseLong(getPpsNodeValue(next), 10));
                        break;
                    case 2:
                        passpointConfiguration.setUsageLimitDataLimit(parseLong(getPpsNodeValue(next), 10));
                        break;
                    case 3:
                        passpointConfiguration.setUsageLimitTimeLimitInMinutes(parseLong(getPpsNodeValue(next), 10));
                        break;
                    default:
                        throw new ParsingException("Unknown node under UsageLimits" + next.getName());
                }
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for UsageLimits");
    }

    private static String[] parseAaaServerTrustedNames(PPSNode pPSNode) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            String[] strArr = null;
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_FQDN)) {
                    strArr = getPpsNodeValue(next).split(NavigationBarInflaterView.GRAVITY_SEPARATOR);
                } else {
                    throw new ParsingException("Unknown node under AAAServerTrustedNames instance: " + next.getName());
                }
            }
            if (strArr != null) {
                return strArr;
            }
            throw new ParsingException("AAAServerTrustedNames instance missing FQDN field");
        }
        throw new ParsingException("Leaf node not expected for AAAServerTrustedNames instance");
    }

    private static void parseVendorWbaExtensionNai(PPSNode pPSNode, PasspointConfiguration passpointConfiguration) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (!name.equals(NODE_DECORATED_IDENTITY_PREFIX)) {
                    Log.w(TAG, "Unknown node under NAI instance: " + next.getName());
                } else if (SdkLevel.isAtLeastS()) {
                    passpointConfiguration.setDecoratedIdentityPrefix(parseDecoratedIdentityPrefix(next));
                }
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for NAI instance");
    }

    private static String parseDecoratedIdentityPrefix(PPSNode pPSNode) throws ParsingException {
        if (pPSNode.isLeaf()) {
            String ppsNodeValue = getPpsNodeValue(pPSNode);
            if (!TextUtils.isEmpty(ppsNodeValue) && ppsNodeValue.endsWith("!")) {
                return ppsNodeValue;
            }
            throw new ParsingException("Invalid value for node DecoratedPrefix");
        }
        throw new ParsingException("Leaf node expected for DecoratedPrefix");
    }

    private static void parseVendorAndroidExtension(PPSNode pPSNode, PasspointConfiguration passpointConfiguration) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (!name.equals(NODE_AAA_SERVER_TRUSTED_NAMES)) {
                    Log.w(TAG, "Unknown node under Android Extension: " + next.getName());
                } else {
                    passpointConfiguration.setAaaServerTrustedNames(parseAaaServerTrustedNames(next));
                }
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for AndroidExtension");
    }

    private static void parseVendorWbaExtension(PPSNode pPSNode, PasspointConfiguration passpointConfiguration) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (!name.equals(NODE_EXTENSION_NAI)) {
                    Log.w(TAG, "Unknown node under WBA Extension: " + next.getName());
                } else {
                    parseVendorWbaExtensionNai(next, passpointConfiguration);
                }
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for WBA Extension");
    }

    private static void parseExtension(PPSNode pPSNode, PasspointConfiguration passpointConfiguration) throws ParsingException {
        if (!pPSNode.isLeaf()) {
            for (PPSNode next : pPSNode.getChildren()) {
                String name = next.getName();
                name.hashCode();
                if (name.equals(NODE_VENDOR_WBA)) {
                    parseVendorWbaExtension(next, passpointConfiguration);
                } else if (!name.equals(NODE_VENDOR_ANDROID)) {
                    Log.w(TAG, "Unknown node under Extension: " + next.getName());
                } else {
                    parseVendorAndroidExtension(next, passpointConfiguration);
                }
            }
            return;
        }
        throw new ParsingException("Leaf node not expected for Extension");
    }

    private static byte[] parseHexString(String str) throws ParsingException {
        if ((str.length() & 1) != 1) {
            int length = str.length() / 2;
            byte[] bArr = new byte[length];
            int i = 0;
            while (i < length) {
                int i2 = i * 2;
                try {
                    bArr[i] = (byte) Integer.parseInt(str.substring(i2, i2 + 2), 16);
                    i++;
                } catch (NumberFormatException unused) {
                    throw new ParsingException("Invalid hex string: " + str);
                }
            }
            return bArr;
        }
        throw new ParsingException("Odd length hex string: " + str + ", length: " + str.length());
    }

    private static long parseDate(String str) throws ParsingException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(str).getTime();
        } catch (ParseException unused) {
            throw new ParsingException("Badly formatted time: " + str);
        }
    }

    private static int parseInteger(String str) throws ParsingException {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            throw new ParsingException("Invalid integer value: " + str);
        }
    }

    private static long parseLong(String str, int i) throws ParsingException {
        try {
            return Long.parseLong(str, i);
        } catch (NumberFormatException unused) {
            throw new ParsingException("Invalid long integer value: " + str);
        }
    }

    private static long[] convertFromLongList(List<Long> list) {
        Long[] lArr = (Long[]) list.toArray(new Long[list.size()]);
        long[] jArr = new long[lArr.length];
        for (int i = 0; i < lArr.length; i++) {
            jArr[i] = lArr[i].longValue();
        }
        return jArr;
    }
}
