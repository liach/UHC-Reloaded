/*
 * The MIT License (MIT)
 *
 * Copyright (c) liachmodded <https://github.com/liachmodded>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package mod.uhcreloaded;

/**
 * @author ThaumicTechTinker Urey.S.Knowledge (a.k.a. 3TUSK/3tusk)
 * This is a mod which brings the original UHC mode rules to 1.8 release
 * Forge Compatible, flexible configuration, and firing contribution :)
 * Sort of meaningless, but I will insist on it, since there used to be an era!
 */

import static mod.uhcreloaded.util.ConfigHandler.allowCraftingGoldenSkull;
import static mod.uhcreloaded.util.ConfigHandler.allowGhastTear;
import static mod.uhcreloaded.util.ConfigHandler.initConfig;
import static mod.uhcreloaded.util.ConfigHandler.openEnderPearlFallingDamage;
import static mod.uhcreloaded.util.ConfigHandler.playerDropSkull;
import static mod.uhcreloaded.util.Misc.MODID;
import static mod.uhcreloaded.util.Misc.NAME;
import static mod.uhcreloaded.util.Misc.VERSION;
import static mod.uhcreloaded.util.Misc.registerBus;

import mod.uhcreloaded.commands.CommandUhcHelp;
import mod.uhcreloaded.commands.CommandUhcMode;
import mod.uhcreloaded.commands.CommandUshcMode;
import mod.uhcreloaded.rules.CancelEnderPearlDamage;
import mod.uhcreloaded.rules.CancelPotionBrewing;
import mod.uhcreloaded.rules.EnforceNoGhastTear;
import mod.uhcreloaded.rules.GoldenItemToGold;
import mod.uhcreloaded.rules.GoldenSkull;
import mod.uhcreloaded.rules.ModdedGoldenStuff;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = MODID, name = NAME, version = VERSION)
public class UhcReloaded {

    public static final Logger LOG = LogManager.getLogger(MODID);
    @Instance(MODID)
    public static UhcReloaded instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File cfgFile = new File(event.getModConfigurationDirectory(), MODID + ".cfg");
        initConfig(cfgFile);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (!allowGhastTear) {
            registerBus(new EnforceNoGhastTear());
        }
        if (playerDropSkull) {
            registerBus(new ModdedGoldenStuff());
        }
        if (!openEnderPearlFallingDamage) {
            registerBus(new CancelEnderPearlDamage());
        }
        registerBus(new CancelPotionBrewing());

        registerBus(new GoldenSkull());
        if (allowCraftingGoldenSkull) {
            new GoldenSkull.SkullRecipe().registerRecipe();
        }

        GoldenItemToGold.regUncraftingGoldenToolsAndArmor();

        ModdedGoldenStuff.removeEnhancedGoldenApple();
        ModdedGoldenStuff.harderGoldenCarrot();
        ModdedGoldenStuff.harderGlisteringMelon();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandUhcMode());
        event.registerServerCommand(new CommandUshcMode());
        event.registerServerCommand(new CommandUhcHelp());
    }
}
