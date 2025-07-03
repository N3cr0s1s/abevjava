package org.bouncycastle.crypto.interfaces;

import org.bouncycastle.crypto.spec.DHParameterSpec;

public interface DHKey {
   DHParameterSpec getParams();
}
