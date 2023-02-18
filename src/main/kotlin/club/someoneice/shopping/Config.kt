package club.someoneice.shopping

import net.minecraftforge.common.ForgeConfigSpec

object Config {
    var isATMShouldTax: ForgeConfigSpec.BooleanValue
    var theTaxRate: ForgeConfigSpec.DoubleValue
    var config: ForgeConfigSpec

    init {
        val builder = ForgeConfigSpec.Builder()
        isATMShouldTax = builder.comment("ATM will collect taxes if this is true.").define("shouldTax", true)
        theTaxRate = builder.comment("ATM will collect taxes in this rates.").defineInRange("taxRate", 0.01, 0.00, 0.80)

        builder.pop(0)
        config = builder.build()
    }
}