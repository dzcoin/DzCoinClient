﻿package org.dzcoinlab.core.types.shamap;


import org.dzcoinlab.core.hash.hash256;

public interface hashedtreewalker {
    public void onleaf(hash256 h, shamapleaf le);
    public void oninner(hash256 h, shamapinner inner);
}


