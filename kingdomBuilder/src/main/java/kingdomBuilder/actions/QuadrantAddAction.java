package kingdomBuilder.actions;

import javafx.util.Pair;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.network.protocol.QuadrantReply;
import kingdomBuilder.redux.Action;

/**
 * Represents the QuadrantAddAction.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class QuadrantAddAction extends Action {
    // TODO: doc
    public final int quadrantId;
    public final Game.TileType[] fieldTypes;

    // TODO: doc
    public QuadrantAddAction(int id, Game.TileType[] fieldTypes) {
        this.quadrantId = id;
        this.fieldTypes = fieldTypes;
    }

    // TODO: doc
    public QuadrantAddAction(QuadrantReply m) {
        Game.TileType[] a = new Game.TileType[100];

        this.quadrantId = m.quadrantId();
        this.fieldTypes = a;

        a[0]  = Game.TileType.valueOf(m.fieldType1());
        a[1]  = Game.TileType.valueOf(m.fieldType2());
        a[2]  = Game.TileType.valueOf(m.fieldType3());
        a[3]  = Game.TileType.valueOf(m.fieldType4());
        a[4]  = Game.TileType.valueOf(m.fieldType5());
        a[5]  = Game.TileType.valueOf(m.fieldType6());
        a[6]  = Game.TileType.valueOf(m.fieldType7());
        a[7]  = Game.TileType.valueOf(m.fieldType8());
        a[8]  = Game.TileType.valueOf(m.fieldType9());
        a[9]  = Game.TileType.valueOf(m.fieldType10());
        a[10] = Game.TileType.valueOf(m.fieldType11());
        a[11] = Game.TileType.valueOf(m.fieldType12());
        a[12] = Game.TileType.valueOf(m.fieldType13());
        a[13] = Game.TileType.valueOf(m.fieldType14());
        a[14] = Game.TileType.valueOf(m.fieldType15());
        a[15] = Game.TileType.valueOf(m.fieldType16());
        a[16] = Game.TileType.valueOf(m.fieldType17());
        a[17] = Game.TileType.valueOf(m.fieldType18());
        a[18] = Game.TileType.valueOf(m.fieldType19());
        a[19] = Game.TileType.valueOf(m.fieldType20());
        a[20] = Game.TileType.valueOf(m.fieldType21());
        a[21] = Game.TileType.valueOf(m.fieldType22());
        a[22] = Game.TileType.valueOf(m.fieldType23());
        a[23] = Game.TileType.valueOf(m.fieldType24());
        a[24] = Game.TileType.valueOf(m.fieldType25());
        a[25] = Game.TileType.valueOf(m.fieldType26());
        a[26] = Game.TileType.valueOf(m.fieldType27());
        a[27] = Game.TileType.valueOf(m.fieldType28());
        a[28] = Game.TileType.valueOf(m.fieldType29());
        a[29] = Game.TileType.valueOf(m.fieldType30());
        a[30] = Game.TileType.valueOf(m.fieldType31());
        a[31] = Game.TileType.valueOf(m.fieldType32());
        a[32] = Game.TileType.valueOf(m.fieldType33());
        a[33] = Game.TileType.valueOf(m.fieldType34());
        a[34] = Game.TileType.valueOf(m.fieldType35());
        a[35] = Game.TileType.valueOf(m.fieldType36());
        a[36] = Game.TileType.valueOf(m.fieldType37());
        a[37] = Game.TileType.valueOf(m.fieldType38());
        a[38] = Game.TileType.valueOf(m.fieldType39());
        a[39] = Game.TileType.valueOf(m.fieldType40());
        a[40] = Game.TileType.valueOf(m.fieldType41());
        a[41] = Game.TileType.valueOf(m.fieldType42());
        a[42] = Game.TileType.valueOf(m.fieldType43());
        a[43] = Game.TileType.valueOf(m.fieldType44());
        a[44] = Game.TileType.valueOf(m.fieldType45());
        a[45] = Game.TileType.valueOf(m.fieldType46());
        a[46] = Game.TileType.valueOf(m.fieldType47());
        a[47] = Game.TileType.valueOf(m.fieldType48());
        a[48] = Game.TileType.valueOf(m.fieldType49());
        a[49] = Game.TileType.valueOf(m.fieldType50());
        a[50] = Game.TileType.valueOf(m.fieldType51());
        a[51] = Game.TileType.valueOf(m.fieldType52());
        a[52] = Game.TileType.valueOf(m.fieldType53());
        a[53] = Game.TileType.valueOf(m.fieldType54());
        a[54] = Game.TileType.valueOf(m.fieldType55());
        a[55] = Game.TileType.valueOf(m.fieldType56());
        a[56] = Game.TileType.valueOf(m.fieldType57());
        a[57] = Game.TileType.valueOf(m.fieldType58());
        a[58] = Game.TileType.valueOf(m.fieldType59());
        a[59] = Game.TileType.valueOf(m.fieldType60());
        a[60] = Game.TileType.valueOf(m.fieldType61());
        a[61] = Game.TileType.valueOf(m.fieldType62());
        a[62] = Game.TileType.valueOf(m.fieldType63());
        a[63] = Game.TileType.valueOf(m.fieldType64());
        a[64] = Game.TileType.valueOf(m.fieldType65());
        a[65] = Game.TileType.valueOf(m.fieldType66());
        a[66] = Game.TileType.valueOf(m.fieldType67());
        a[67] = Game.TileType.valueOf(m.fieldType68());
        a[68] = Game.TileType.valueOf(m.fieldType69());
        a[69] = Game.TileType.valueOf(m.fieldType70());
        a[70] = Game.TileType.valueOf(m.fieldType71());
        a[71] = Game.TileType.valueOf(m.fieldType72());
        a[72] = Game.TileType.valueOf(m.fieldType73());
        a[73] = Game.TileType.valueOf(m.fieldType74());
        a[74] = Game.TileType.valueOf(m.fieldType75());
        a[75] = Game.TileType.valueOf(m.fieldType76());
        a[76] = Game.TileType.valueOf(m.fieldType77());
        a[77] = Game.TileType.valueOf(m.fieldType78());
        a[78] = Game.TileType.valueOf(m.fieldType79());
        a[79] = Game.TileType.valueOf(m.fieldType80());
        a[80] = Game.TileType.valueOf(m.fieldType81());
        a[81] = Game.TileType.valueOf(m.fieldType82());
        a[82] = Game.TileType.valueOf(m.fieldType83());
        a[83] = Game.TileType.valueOf(m.fieldType84());
        a[84] = Game.TileType.valueOf(m.fieldType85());
        a[85] = Game.TileType.valueOf(m.fieldType86());
        a[86] = Game.TileType.valueOf(m.fieldType87());
        a[87] = Game.TileType.valueOf(m.fieldType88());
        a[88] = Game.TileType.valueOf(m.fieldType89());
        a[89] = Game.TileType.valueOf(m.fieldType90());
        a[90] = Game.TileType.valueOf(m.fieldType91());
        a[91] = Game.TileType.valueOf(m.fieldType92());
        a[92] = Game.TileType.valueOf(m.fieldType93());
        a[93] = Game.TileType.valueOf(m.fieldType94());
        a[94] = Game.TileType.valueOf(m.fieldType95());
        a[95] = Game.TileType.valueOf(m.fieldType96());
        a[96] = Game.TileType.valueOf(m.fieldType97());
        a[97] = Game.TileType.valueOf(m.fieldType98());
        a[98] = Game.TileType.valueOf(m.fieldType99());
        a[99] = Game.TileType.valueOf(m.fieldType100());
    }
}
