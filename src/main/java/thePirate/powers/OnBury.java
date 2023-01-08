package thePirate.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.List;

public interface OnBury {

    public abstract void onBury(AbstractCard card);

    public abstract void onBuryCards(List<AbstractCard> cards);
}
