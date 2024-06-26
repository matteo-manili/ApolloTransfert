package com.apollon.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.generic.FormatConfig;

/**
 * Tool for doing currency formatting
 */
@DefaultKey(value = "currency")
public final class VelocityCurrency extends FormatConfig {

    /**
     * Constructor
     */
    public VelocityCurrency() {
    }

    /**
     * @param money The {@link Money} object to format
     * @return
     */
    public String format(BigDecimal money) {
		/**
		 * Running the previous code example results in the output that follows. The formatted number, which is in the second column, varies with Locale:
		
		###,###.###      123,456.789     en_US
		###,###.###      123.456,789     de_DE
		###,###.###      123 456,789     fr_FR
		 */
    	NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
		DecimalFormat df = (DecimalFormat)nf;
		//df.applyPattern("###,###.###");
		String output = df.format(money);
		return output;
    }
}
