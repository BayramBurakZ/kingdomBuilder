package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.lang.reflect.Field;

/**
 * Represents message that contains details of one quadrant -
 * reply message ?quadrant {@link kingdomBuilder.network.protocol.QuadrantRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?quadrant) <[#{quadrantId};#{fieldType1};#{fieldType2};#{fieldType3};#{fieldType4};" +
        "#{fieldType5};#{fieldType6};#{fieldType7};#{fieldType8};#{fieldType9};#{fieldType10};#{fieldType11};" +
        "#{fieldType12};#{fieldType13};#{fieldType14};#{fieldType15};#{fieldType16};#{fieldType17};#{fieldType18};" +
        "#{fieldType19};#{fieldType20};#{fieldType21};#{fieldType22};#{fieldType23};#{fieldType24};#{fieldType25};" +
        "#{fieldType26};#{fieldType27};#{fieldType28};#{fieldType29};#{fieldType30};#{fieldType31};#{fieldType32};" +
        "#{fieldType33};#{fieldType34};#{fieldType35};#{fieldType36};#{fieldType37};#{fieldType38};#{fieldType39};" +
        "#{fieldType40};#{fieldType41};#{fieldType42};#{fieldType43};#{fieldType44};#{fieldType45};#{fieldType46};" +
        "#{fieldType47};#{fieldType48};#{fieldType49};#{fieldType50};#{fieldType51};#{fieldType52};#{fieldType53};" +
        "#{fieldType54};#{fieldType55};#{fieldType56};#{fieldType57};#{fieldType58};#{fieldType59};#{fieldType60};" +
        "#{fieldType61};#{fieldType62};#{fieldType63};#{fieldType64};#{fieldType65};#{fieldType66};#{fieldType67};" +
        "#{fieldType68};#{fieldType69};#{fieldType70};#{fieldType71};#{fieldType72};#{fieldType73};#{fieldType74};" +
        "#{fieldType75};#{fieldType76};#{fieldType77};#{fieldType78};#{fieldType79};#{fieldType80};#{fieldType81};" +
        "#{fieldType82};#{fieldType83};#{fieldType84};#{fieldType85};#{fieldType86};#{fieldType87};#{fieldType88};" +
        "#{fieldType89};#{fieldType90};#{fieldType91};#{fieldType92};#{fieldType93};#{fieldType94};#{fieldType95};" +
        "#{fieldType96};#{fieldType97};#{fieldType98};#{fieldType99};#{fieldType100}]>")
public record QuadrantReply(
        int quadrantId,
        String fieldType1,
        String fieldType2,
        String fieldType3,
        String fieldType4,
        String fieldType5,
        String fieldType6,
        String fieldType7,
        String fieldType8,
        String fieldType9,
        String fieldType10,
        String fieldType11,
        String fieldType12,
        String fieldType13,
        String fieldType14,
        String fieldType15,
        String fieldType16,
        String fieldType17,
        String fieldType18,
        String fieldType19,
        String fieldType20,
        String fieldType21,
        String fieldType22,
        String fieldType23,
        String fieldType24,
        String fieldType25,
        String fieldType26,
        String fieldType27,
        String fieldType28,
        String fieldType29,
        String fieldType30,
        String fieldType31,
        String fieldType32,
        String fieldType33,
        String fieldType34,
        String fieldType35,
        String fieldType36,
        String fieldType37,
        String fieldType38,
        String fieldType39,
        String fieldType40,
        String fieldType41,
        String fieldType42,
        String fieldType43,
        String fieldType44,
        String fieldType45,
        String fieldType46,
        String fieldType47,
        String fieldType48,
        String fieldType49,
        String fieldType50,
        String fieldType51,
        String fieldType52,
        String fieldType53,
        String fieldType54,
        String fieldType55,
        String fieldType56,
        String fieldType57,
        String fieldType58,
        String fieldType59,
        String fieldType60,
        String fieldType61,
        String fieldType62,
        String fieldType63,
        String fieldType64,
        String fieldType65,
        String fieldType66,
        String fieldType67,
        String fieldType68,
        String fieldType69,
        String fieldType70,
        String fieldType71,
        String fieldType72,
        String fieldType73,
        String fieldType74,
        String fieldType75,
        String fieldType76,
        String fieldType77,
        String fieldType78,
        String fieldType79,
        String fieldType80,
        String fieldType81,
        String fieldType82,
        String fieldType83,
        String fieldType84,
        String fieldType85,
        String fieldType86,
        String fieldType87,
        String fieldType88,
        String fieldType89,
        String fieldType90,
        String fieldType91,
        String fieldType92,
        String fieldType93,
        String fieldType94,
        String fieldType95,
        String fieldType96,
        String fieldType97,
        String fieldType98,
        String fieldType99,
        String fieldType100
) {

    String at(int index) {
        Field field;
        try {
            field = getClass().getField("fieldType" + index);
            return (String) field.get(this);
        }
        catch(Exception unused) { throw new ArrayIndexOutOfBoundsException(); }
    }

    void setAt(int index, String value) {
        Field field;
        try {
            field = getClass().getField("fieldType" + index);
            field.set(this, value);
        } catch(Exception unused) { throw new ArrayIndexOutOfBoundsException(); }
    }

}
