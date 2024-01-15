package com.maciej.ants;

import com.badlogic.gdx.math.Vector2;

/**
 * Vector2 positions unrelated to screen dimensions.
 */
public interface AbstractPositionable {
    /**
     * Set abstract position.
     * @param abstractPosition Value to be set as a position.
     */
    public void setAbstractPosition(Vector2 abstractPosition);

    /**
     *
     * @return Vector2 representing abstract position.
     */
    public Vector2 getAbstractPosition();
}
