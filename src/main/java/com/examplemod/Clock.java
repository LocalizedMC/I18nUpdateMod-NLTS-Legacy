package com.examplemod;

import static com.mumfrey.liteloader.gl.GL.*;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.ReadableColor;

import java.util.Calendar;

/**
 * Simple implementation of an analogue clock to demonstrate how to LiteLoader
 * all the things 
 *
 * @author Adam Mummery-Smith
 */
public class Clock
{
    /**
     * This is the clock face resource, you need to create a resource location
     * for any assets that you wish to use. It is best to make these static to
     * avoid new instances being created for every instance of the referencing
     * object, this also means they will only be garbage collected when the
     * class is garbage collected or when no instances of the class are left.
     * 
     * <p>The first parameter for the resource location is the "domain" and this
     * should normally be your mod's name. The domain MUST be lower case! The
     * second is the resource "path" and represents the path to the resource
     * within the domain. It is convention that the path always start with the
     * resource type, such as "textures" in this case.</p>
     * 
     * <p>Resources are always stored in a path of the form
     * "assets/{domain}/{path}" which makes the appropriate path to the
     * CLOCKFACE resource: "/assets/example/textures/clock/face.png"</p>
     */
    private static final ResourceLocation CLOCKFACE = new ResourceLocation("example", "textures/clock/face.png");
    
    /**
     * Angles for the hands
     */
    private float smallHandAngle, largeHandAngle, secondHandAngle;
    
    /**
     * Sizes for each of the hands
     */
    private float smallHandSize, largeHandSize, secondHandSize;
    
    /**
     * Width of the hands 
     */
    private float handWidth = 1.0F;
    
    /**
     * Colours for each of the hands
     */
    private ReadableColor smallHandColour, largeHandColour, secondHandColour;
    
    /**
     * Size and position for the clock
     */
    private int xPos, yPos, size;
    
    /**
     * Whether the clock is currently visible
     */
    private boolean visible = true;
    
    /**
     * @param xPos X position for the clock
     * @param yPos Y position for the clock
     */
    public Clock(int xPos, int yPos)
    {
        this.setPosition(xPos, yPos);
        this.setSize(64);
        
        this.largeHandColour  = ReadableColor.WHITE;   
        this.smallHandColour  = ReadableColor.GREY;
        this.secondHandColour = ReadableColor.ORANGE;
    }
    
    /**
     * @param xPos
     * @param yPos
     */
    public void setPosition(int xPos, int yPos)
    {
        this.xPos = Math.max(0,  xPos);
        this.yPos = Math.max(0,  yPos);
    }
    
    /**
     * Set the size of the clock
     * 
     * @param size new size (min is 32)
     */
    public void setSize(int size)
    {
        this.size  = Math.max(32, size);
        
        this.smallHandSize  = this.size * 0.25F;
        this.largeHandSize  = this.size * 0.38F;
        this.secondHandSize = this.size * 0.35F;
        
        this.handWidth = this.size / 64.0F;
    }
    
    /**
     * Get the current size
     */
    public int getSize()
    {
        return this.size;
    }
    
    /**
     * Get whether the clock is currently visible
     */
    public boolean isVisible()
    {
        return this.visible;
    }
    
    /**
     * Set whether the clock should be visible
     * 
     * @param visible new visibility setting
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
    
    /**
     * Render the clock at its current position, unless hidden
     * 
     * @param minecraft Minecraft game instance
     */
    public void render(Minecraft minecraft)
    {
        if (this.isVisible())
        {
            // First, update the hand angles
            this.calculateAngles();
            
            // Then render the actual clock
            this.renderClock(minecraft);
        }
    }
    
    /**
     * Gets the current time and calculates the angles for the clock hands
     */
    private void calculateAngles()
    {
        Calendar calendar = Calendar.getInstance();
        
        int hour   = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        
        this.smallHandAngle  = 360.0F * (0.0833F * hour + 0.00138F * minute);
        this.largeHandAngle  = 360.0F * (0.0166F * minute);
        this.secondHandAngle = 360.0F * (0.0166F * second);
    }
    
    /**
     * Renders the clock
     * 
     * @param minecraft Minecraft game instance
     */
    private void renderClock(Minecraft minecraft)
    {
        // Render the face
        this.renderClockFace(minecraft);
        
        // Render each of the hands
        this.renderClockHand(this.largeHandAngle,  this.largeHandSize,  this.handWidth * 1.2F, this.largeHandColour);
        this.renderClockHand(this.smallHandAngle,  this.smallHandSize,  this.handWidth * 2.0F, this.smallHandColour);
        this.renderClockHand(this.secondHandAngle, this.secondHandSize, this.handWidth * 1.2F, this.secondHandColour);
    }
    
    /**
     * Renders the clock face texture using the texture resource
     * 
     * @param minecraft Minecraft game instance
     */
    private void renderClockFace(Minecraft minecraft)
    {
        // Bind the texture resource
        minecraft.getTextureManager().bindTexture(Clock.CLOCKFACE);
        
        // Draw a rectangle using the currently bound texture
        glDrawTexturedRect(this.xPos, this.yPos, this.size, this.size, 1, 1, 511, 511);
    }
    
    /**
     * Render one of the hands 
     */
    private void renderClockHand(float angle, float length, float width, ReadableColor colour)
    {
        // Push the current transform onto the stack
        glPushMatrix();
        
        // Transform to the mid point of the clock
        glTranslatef(this.xPos + (this.size / 2), this.yPos + (this.size / 2), 0);
        
        // and rotate by the hand angle
        glRotatef(angle, 0.0F, 0.0F, 1.0F);
        
        // then draw the hand (straight up of course)
        glDrawRect(width * -0.5F, length * 0.2F, width * 0.5F, -length, colour);
        
        // and finally restore the current transform
        glPopMatrix();
    }
    
    /**
     * Draw a rectangle using the currently bound texture
     */
    private static void glDrawTexturedRect(int x, int y, int width, int height, int u, int v, int u2, int v2)
    {
        // Set the appropriate OpenGL modes
        glDisableLighting();
        glDisableBlend();
        glAlphaFunc(GL_GREATER, 0.01F);
        glEnableTexture2D();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        float texMapScale = 0.001953125F; // 512px
        
        // We use the tessellator rather than drawing individual quads because
        // it uses vertex arrays to draw the quads more efficiently.
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL_QUADS, POSITION_TEX);
        worldRenderer.pos(x + 0,     y + height, 0).tex(u  * texMapScale, v2 * texMapScale).endVertex();
        worldRenderer.pos(x + width, y + height, 0).tex(u2 * texMapScale, v2 * texMapScale).endVertex();
        worldRenderer.pos(x + width, y + 0,      0).tex(u2 * texMapScale, v  * texMapScale).endVertex();
        worldRenderer.pos(x + 0,     y + 0,      0).tex(u  * texMapScale, v  * texMapScale).endVertex();
        tessellator.draw();
    }
    
    /**
     * Draw an opaque rectangle
     */
    private static void glDrawRect(float x1, float y1, float x2, float y2, ReadableColor colour)
    {
        // Set GL modes
        glDisableBlend();
        glDisableTexture2D();
        glDisableCulling();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(colour.getRed(), colour.getGreen(), colour.getBlue(), 1.0F);
        
        // Draw the quad
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL_QUADS, POSITION);
        worldRenderer.pos(x1, y2, 0).endVertex();
        worldRenderer.pos(x2, y2, 0).endVertex();
        worldRenderer.pos(x2, y1, 0).endVertex();
        worldRenderer.pos(x1, y1, 0).endVertex();
        tessellator.draw();
        
        // Restore GL modes
        glEnableCulling();
        glEnableTexture2D();
    }
}
