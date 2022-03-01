package kingdomBuilder.actions;

import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.network.protocol.QuadrantReply;
import kingdomBuilder.redux.Action;

/**
 * Represents the QuadrantAddAction.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class QuadrantAddAction extends Action {

    /**
     * Represents the id of the quadrant.
     */
    public final int quadrantId;

    /**
     * Represents field type for every field on the quadrant.
     */
    public final TileType[] fieldTypes;

    /**
     * Creates a new QuadrantAddAction with the given id and the types.
     * @param id the id of the quadrant.
     * @param fieldTypes the types of every field on the quadrant.
     */
    public QuadrantAddAction(int id, TileType[] fieldTypes) {
        this.quadrantId = id;
        this.fieldTypes = fieldTypes;
    }

    /**
     * Creates a new QuadrantAddAction with the given QuadrantReply message.
     * @param m a QuadrantReply message.
     */
    public QuadrantAddAction(QuadrantReply m) {
        TileType[] a = new TileType[100];

        this.quadrantId = m.quadrantId();
        this.fieldTypes = a;

        a[0]  = TileType.valueOf(m.fieldType1());
        a[1]  = TileType.valueOf(m.fieldType2());
        a[2]  = TileType.valueOf(m.fieldType3());
        a[3]  = TileType.valueOf(m.fieldType4());
        a[4]  = TileType.valueOf(m.fieldType5());
        a[5]  = TileType.valueOf(m.fieldType6());
        a[6]  = TileType.valueOf(m.fieldType7());
        a[7]  = TileType.valueOf(m.fieldType8());
        a[8]  = TileType.valueOf(m.fieldType9());
        a[9]  = TileType.valueOf(m.fieldType10());
        a[10] = TileType.valueOf(m.fieldType11());
        a[11] = TileType.valueOf(m.fieldType12());
        a[12] = TileType.valueOf(m.fieldType13());
        a[13] = TileType.valueOf(m.fieldType14());
        a[14] = TileType.valueOf(m.fieldType15());
        a[15] = TileType.valueOf(m.fieldType16());
        a[16] = TileType.valueOf(m.fieldType17());
        a[17] = TileType.valueOf(m.fieldType18());
        a[18] = TileType.valueOf(m.fieldType19());
        a[19] = TileType.valueOf(m.fieldType20());
        a[20] = TileType.valueOf(m.fieldType21());
        a[21] = TileType.valueOf(m.fieldType22());
        a[22] = TileType.valueOf(m.fieldType23());
        a[23] = TileType.valueOf(m.fieldType24());
        a[24] = TileType.valueOf(m.fieldType25());
        a[25] = TileType.valueOf(m.fieldType26());
        a[26] = TileType.valueOf(m.fieldType27());
        a[27] = TileType.valueOf(m.fieldType28());
        a[28] = TileType.valueOf(m.fieldType29());
        a[29] = TileType.valueOf(m.fieldType30());
        a[30] = TileType.valueOf(m.fieldType31());
        a[31] = TileType.valueOf(m.fieldType32());
        a[32] = TileType.valueOf(m.fieldType33());
        a[33] = TileType.valueOf(m.fieldType34());
        a[34] = TileType.valueOf(m.fieldType35());
        a[35] = TileType.valueOf(m.fieldType36());
        a[36] = TileType.valueOf(m.fieldType37());
        a[37] = TileType.valueOf(m.fieldType38());
        a[38] = TileType.valueOf(m.fieldType39());
        a[39] = TileType.valueOf(m.fieldType40());
        a[40] = TileType.valueOf(m.fieldType41());
        a[41] = TileType.valueOf(m.fieldType42());
        a[42] = TileType.valueOf(m.fieldType43());
        a[43] = TileType.valueOf(m.fieldType44());
        a[44] = TileType.valueOf(m.fieldType45());
        a[45] = TileType.valueOf(m.fieldType46());
        a[46] = TileType.valueOf(m.fieldType47());
        a[47] = TileType.valueOf(m.fieldType48());
        a[48] = TileType.valueOf(m.fieldType49());
        a[49] = TileType.valueOf(m.fieldType50());
        a[50] = TileType.valueOf(m.fieldType51());
        a[51] = TileType.valueOf(m.fieldType52());
        a[52] = TileType.valueOf(m.fieldType53());
        a[53] = TileType.valueOf(m.fieldType54());
        a[54] = TileType.valueOf(m.fieldType55());
        a[55] = TileType.valueOf(m.fieldType56());
        a[56] = TileType.valueOf(m.fieldType57());
        a[57] = TileType.valueOf(m.fieldType58());
        a[58] = TileType.valueOf(m.fieldType59());
        a[59] = TileType.valueOf(m.fieldType60());
        a[60] = TileType.valueOf(m.fieldType61());
        a[61] = TileType.valueOf(m.fieldType62());
        a[62] = TileType.valueOf(m.fieldType63());
        a[63] = TileType.valueOf(m.fieldType64());
        a[64] = TileType.valueOf(m.fieldType65());
        a[65] = TileType.valueOf(m.fieldType66());
        a[66] = TileType.valueOf(m.fieldType67());
        a[67] = TileType.valueOf(m.fieldType68());
        a[68] = TileType.valueOf(m.fieldType69());
        a[69] = TileType.valueOf(m.fieldType70());
        a[70] = TileType.valueOf(m.fieldType71());
        a[71] = TileType.valueOf(m.fieldType72());
        a[72] = TileType.valueOf(m.fieldType73());
        a[73] = TileType.valueOf(m.fieldType74());
        a[74] = TileType.valueOf(m.fieldType75());
        a[75] = TileType.valueOf(m.fieldType76());
        a[76] = TileType.valueOf(m.fieldType77());
        a[77] = TileType.valueOf(m.fieldType78());
        a[78] = TileType.valueOf(m.fieldType79());
        a[79] = TileType.valueOf(m.fieldType80());
        a[80] = TileType.valueOf(m.fieldType81());
        a[81] = TileType.valueOf(m.fieldType82());
        a[82] = TileType.valueOf(m.fieldType83());
        a[83] = TileType.valueOf(m.fieldType84());
        a[84] = TileType.valueOf(m.fieldType85());
        a[85] = TileType.valueOf(m.fieldType86());
        a[86] = TileType.valueOf(m.fieldType87());
        a[87] = TileType.valueOf(m.fieldType88());
        a[88] = TileType.valueOf(m.fieldType89());
        a[89] = TileType.valueOf(m.fieldType90());
        a[90] = TileType.valueOf(m.fieldType91());
        a[91] = TileType.valueOf(m.fieldType92());
        a[92] = TileType.valueOf(m.fieldType93());
        a[93] = TileType.valueOf(m.fieldType94());
        a[94] = TileType.valueOf(m.fieldType95());
        a[95] = TileType.valueOf(m.fieldType96());
        a[96] = TileType.valueOf(m.fieldType97());
        a[97] = TileType.valueOf(m.fieldType98());
        a[98] = TileType.valueOf(m.fieldType99());
        a[99] = TileType.valueOf(m.fieldType100());
    }
}
