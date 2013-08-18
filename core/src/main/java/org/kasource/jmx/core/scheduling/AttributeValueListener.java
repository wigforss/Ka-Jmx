package org.kasource.jmx.core.scheduling;

import org.kasource.jmx.core.bean.ManagedAttributeValue;

public interface AttributeValueListener {

    public void onValueChange(ManagedAttributeValue value);
}
