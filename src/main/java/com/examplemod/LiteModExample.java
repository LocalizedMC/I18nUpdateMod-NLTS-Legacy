package com.examplemod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.io.File;

/**
 * This is a very simple example LiteMod, it draws an analogue clock on the
 * minecraft HUD using a traditional onTick hook supplied by LiteLoader's
 * {@link Tickable} interface.
 *
 * @author Adam Mummery-Smith
 */
@ExposableOptions(strategy = ConfigStrategy.Versioned, filename="examplemod.json")
public class LiteModExample implements Tickable, Configurable
{
    /**
     * This is our instance of Clock which we will draw every tick
     */
    private Clock clock = new Clock(10, 10);
    
    /**
     * This is a keybinding that we will register with the game and use to
     * toggle the clock
     * 
     * Notice that we specify the key name as an *unlocalised* string. The
     * localisation is provided from the included resource file.
     */
    private static KeyBinding clockKeyBinding = new KeyBinding("key.clock.toggle", Keyboard.KEY_F12, "key.categories.litemods");
    
    @Expose
    @SerializedName("clock_size")
    private int clockSize = 64;
    
    @Expose
    @SerializedName("clock_visible")
    private boolean clockVisible = true;
    
    /**
     * Default constructor. All LiteMods must have a default constructor. In
     * general you should do very little in the mod constructor EXCEPT for
     * initialising any non-game-interfacing components or performing sanity
     * checking prior to initialisation
     */
    public LiteModExample()
    {
    }
    
    /**
     * getName() should be used to return the display name of your mod and MUST
     * NOT return null
     * 
     * @see com.mumfrey.liteloader.LiteMod#getName()
     */
    @Override
    public String getName()
    {
        return "Example Mod";
    }
    
    /**
     * getVersion() should return the same version string present in the mod
     * metadata, although this is not a strict requirement.
     * 
     * @see com.mumfrey.liteloader.LiteMod#getVersion()
     */
    @Override
    public String getVersion()
    {
        return "0.0.0";
    }
    
    @Override
    public Class<? extends ConfigPanel> getConfigPanelClass()
    {
        return ExampleModConfigPanel.class;
    }
    
    /**
     * init() is called very early in the initialisation cycle, before the game
     * is fully initialised, this means that it is important that your mod does
     * not interact with the game in any way at this point.
     * 
     * @see com.mumfrey.liteloader.LiteMod#init(java.io.File)
     */
    @Override
    public void init(File configPath)
    {
        // The key binding declared above won't do anything unless we register
        // it, LiteLoader's Input manager provides a convenience method for this
        LiteLoader.getInput().registerKeyBinding(LiteModExample.clockKeyBinding);
        
        this.clock.setSize(this.clockSize);
        this.clock.setVisible(this.clockVisible);
    }
    
    /**
     * upgradeSettings is used to notify a mod that its version-specific
     * settings are being migrated
     * 
     * @see com.mumfrey.liteloader.LiteMod#upgradeSettings(java.lang.String,
     *         java.io.File, java.io.File)
     */
    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath)
    {
    }
    
    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        // The three checks here are critical to ensure that we only draw the
        // clock as part of the "HUD" and don't draw it over active GUI's or
        // other elements
        if (inGame && minecraft.currentScreen == null && Minecraft.isGuiEnabled())
        {
            if (LiteModExample.clockKeyBinding.isPressed())
            {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
                {
                    this.clockSize = (this.clockSize << 1) & 0x1FF;
                    this.clock.setSize(this.clockSize);
                    this.clockSize = this.clock.getSize();
                }
                else
                {
                    this.clock.setVisible(!this.clock.isVisible());
                    this.clockVisible = this.clock.isVisible();
                }
                
                // Our @Expose annotations control what properties get saved,
                // this tells liteloader to actually write properties to disk
                LiteLoader.getInstance().writeConfig(this);
            }
            
            // Render the clock
            this.clock.render(minecraft);
        }
    }
    
    boolean getClockVisibility()
    {
        return this.clock.isVisible();
    }
    
    void setClockVisibility(boolean visible)
    {
        this.clock.setVisible(this.clockVisible = visible);
    }
}
