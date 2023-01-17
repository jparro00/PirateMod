package thePirate.patches.dungeons;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import thePirate.cards.lures.AbstractLure;

import java.util.ArrayList;

public class LureSpawnPatch {
    public LureSpawnPatch(){

    }

    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "getCard",
            paramtypez={
                    AbstractCard.CardRarity.class
            }
    )
    public static class GetCardPatch{

        @SpireInsertPatch(
                locator=Locator.class
        )
        public static SpireReturn<AbstractCard> Insert(AbstractCard.CardRarity rarity){

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
            tmp.group.addAll(AbstractDungeon.uncommonCardPool.group);
            tmp.group.addAll(AbstractLure.getSpawnableLures());
            return SpireReturn.Return(tmp.getRandomCard(true));

        }

    }
    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "getCard",
            paramtypez={
                    AbstractCard.CardRarity.class,
                    Random.class
            }
    )
    public static class GetCardRngPatch{

        @SpireInsertPatch(
                locator=Locator.class
        )
        public static SpireReturn<AbstractCard> Insert(AbstractCard.CardRarity rarity, Random rng){

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
            tmp.group.addAll(AbstractDungeon.uncommonCardPool.group);
            tmp.group.addAll(AbstractLure.getSpawnableLures());
            return SpireReturn.Return(tmp.getRandomCard(rng));

        }


    }
    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws PatchingException, CannotCompileException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "uncommonCardPool");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }

}
