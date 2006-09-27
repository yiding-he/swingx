/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swingx;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import org.jdesktop.swingx.plaf.JXStatusBarAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.StatusBarUI;

/**
 * <p>A container for <code>JComponents</code> that is typically placed at
 * the bottom of a form and runs the entire width of the form. There are 3
 * important functions that <code>JXStatusBar</code> provides.
 * First, <code>JXStatusBar</code> provides a hook for a pluggable look.
 * There is a definite look associated with status bars on windows, for instance.
 * By implementing a subclass of {@link JComponent}, we provide a way for the
 * pluggable look and feel system to modify the look of the status bar.</p>
 *
 * <p>Second, <code>JXStatusBar</code> comes with its own layout manager. Each item is added to
 * the <code>JXStatusBar</code> with a <code>JXStatusBar.Constraint</code>
 * as the constraint argument. The <code>JXStatusBar.Constraint</code> contains 
 * an <code>Insets</code> object, as well as a <code>ResizeBehavior</code>, 
 * which can be FIXED or FILL. The resize behaviour applies to the width of
 * components. All components added will maintain there preferred height, and the
 * height of the <code>JXStatusBar</code> will be the height of the highest 
 * component plus insets.</p>
 *  
 * <p>A constraint with <code>JXStatusBar.Constraint.ResizeBehavior.FIXED</code>
 * will cause the component to occupy a fixed area on the <code>JXStatusBar</code>.
 * The size of the area remains constant when the <code>JXStatusBar</code> is resized.
 * A constraint with this behavior may also take a width value, see 
 * {@link JXStatusBar.Constraint#setPreferredWidth(int)}. The width is a preferred
 * minimum width. If the component preferred width is greater than the constraint
 * width, the component width will apply.</p>
 * 
 * <p>All components with constraint <code>JXStatusBar.Constraint.ResizeBehavior.FILL</code>
 * will share equally any spare space in the <code>JXStatusBar</code>. Spare space
 * is that left over after allowing for all FIXED component and the preferred 
 * width of FILL components, plus insets  
 * 
 * <p>Constructing a <code>JXStatusBar</code> is very straitforward:
 * <pre><code>
 *      JXStatusBar bar = new JXStatusBar();
 *      JLabel statusLabel = new JLabel("Ready");
 *      JXStatusBar.Constraint c1 = new JXStatusBarConstraint() 
 *      c1.setPreferredWidth = 100;
 *      bar.add(statusLabel, c1);     // Fixed width of 100 with no inserts
 *      JXStatusBar.Constraint c2 = new JXStatusBarConstraint(
 *      		JXStatusBar.Constraint.ResizeBehavior.FILL) // Fill with no inserts
 *      JProgressBar pbar = new JProgressBar();
 *      bar.add(pbar, c2);            // Fill with no inserts - will use remaining space
 * </code></pre></p>
 *
 * <p>Two common use cases for status bars include tracking application status and
 * progress. <code>JXStatusBar</code> does not manage these tasks, but instead special components
 * exist or can be created that do manage these tasks. For example, if your application
 * has a TaskManager or some other repository of currently running jobs, you could
 * easily create a TaskManagerProgressBar that tracks those jobs. This component
 * could then be added to the <code>JXStatusBar</code> like any other component.</p>
 *
 * @author pdoubleya
 * @author rbair
 */
public class JXStatusBar extends JXPanel {
    /**
     * @see #getUIClassID
     * @see #readObject
     */
    public static final String uiClassID = "StatusBarUI";
    
    /**
     * Initialization that would ideally be moved into various look and feel
     * classes.
     */
    static {
        LookAndFeelAddons.contribute(new JXStatusBarAddon());
    }
    
    /**
     * Creates a new JXStatusBar
     */
    public JXStatusBar() {
        super();
    }

    /**
     * Returns the look and feel (L&F) object that renders this component.
     * 
     * @return the StatusBarUI object that renders this component
     */
    @Override
    public StatusBarUI getUI() {
        return (StatusBarUI) ui;
    }

    /**
     * Sets the look and feel (L&F) object that renders this component.
     * 
     * @param ui
     *            the StatusBarUI L&F object
     * @see javax.swing.UIDefaults#getUI
     * @beaninfo bound: true hidden: true attribute: visualUpdate true
     *           description: The UI object that implements the Component's
     *           LookAndFeel.
     */
    public void setUI(StatusBarUI ui) {
        super.setUI(ui);
    }

    /**
     * Returns a string that specifies the name of the L&F class that renders
     * this component.
     * 
     * @return "StatusBarUI"
     * @see javax.swing.JComponent#getUIClassID
     * @see javax.swing.UIDefaults#getUI
     * @beaninfo expert: true description: A string that specifies the name of
     *           the L&F class.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Notification from the <code>UIManager</code> that the L&F has changed.
     * Replaces the current UI object with the latest version from the
     * <code>UIManager</code>.
     * 
     * @see javax.swing.JComponent#updateUI
     */
    @Override
    public void updateUI() {
        setUI((StatusBarUI) LookAndFeelAddons
                .getUI(this, StatusBarUI.class));
    }

    /**
     * The constraint object to be used with the <code>JXStatusBar</code>. It takes
     * a ResizeBehaviour, Insets and a Width. Width is only applicable for  
     * ResizeBehavior.FIXED. @see JXStatusBar class documentation.
     */
    public static class Constraint {
        public static enum ResizeBehavior {FILL, FIXED}
        
        private Insets insets;
        private ResizeBehavior resizeBehavior;
        private int preferredWidth = 0;
        
        /**
         * Creates a new Constraint with default FIXED behaviour and no insets.
         */
        public Constraint() {
            this(ResizeBehavior.FIXED, null);
        }
        
        /**
         * Creates a new Constraint with default FIXED behaviour and the given insets
         * 
         * @param insets may be null. If null, an Insets with 0 values will be used.
         */
        public Constraint(Insets insets) {
            this(ResizeBehavior.FIXED, insets);
        }
        
        /**
         * Creates a new Constraint with default FIXED behaviour and the given minimum
         * preferred width.
         * 
         * @param minPrefWidth must be >= 0
         */
        public Constraint(int minPrefWidth) {
            this(minPrefWidth, null);
        }
        
        /**
         * Creates a new Constraint with default FIXED behaviour and the given minimum
         * preferred width, and using the given Insets.
         * 
         * @param minPrefWidth must be >= 0
         * @param insets may be null. If null, an Insets with 0 values will be used.
         */
        public Constraint(int minPrefWidth, Insets insets) {
            if (minPrefWidth < 0) {
                throw new IllegalArgumentException("minPrefSize must be >= 0");
            }
            this.preferredWidth = minPrefWidth;
            this.insets = insets == null ? new Insets(0, 0, 0, 0) : (Insets)insets.clone();
            this.resizeBehavior = resizeBehavior.FIXED;
        }
        
        /**
         * Creates a new Constraint with the specified resize behaviour and no insets
         * 
         * @param resizeBehavior - either JXStatusBar.Constraint.ResizeBehavior.FIXED
         * or JXStatusBar.Constraint.ResizeBehavior.FILL.
         */
        public Constraint(ResizeBehavior resizeBehavior) {
            this(resizeBehavior, null);
        }
        
        /**
         * Creates a new Constraint with the specified resize behavior and insets.
         * 
         * @param resizeBehavior - either JXStatusBar.Constraint.ResizeBehavior.FIXED
         * or JXStatusBar.Constraints.ResizeBehavior.FILL.
         * @param insets may be null. If null, an Insets with 0 values will be used.
         */
        public Constraint(ResizeBehavior resizeBehavior, Insets insets) {
            this.resizeBehavior = resizeBehavior;
            this.insets = insets == null ? new Insets(0, 0, 0, 0) : (Insets)insets.clone();
        }
        
        /**
         * Set the preferred minimum width the component added with this 
         * constraint will occupy on the <code>JXStatusBar</code>. Only applies
         * to ResizeBehavior.FIXED. Will be ignored for ResizeBehavior.FILL.
         * If component preferred size width is greater than this width, 
         * component preferred width will be used.
         *  
         * @param width - minimum width component will occupy. If 0, preferred
         * width of the component will be used. This width includes any insets.
         * The width specified must be >= 0
         */
        public void setPreferredWidth(int width) {
            if (width < 0) {
                throw new IllegalArgumentException("width must be >= 0");
            }
            preferredWidth = resizeBehavior == ResizeBehavior.FIXED ? width : 0;
        }
        
        /**
         * Returns the ResizeBehavior.
         * 
         * @return ResizeBehavior
         */
        public ResizeBehavior getResizeBehavior() {
            return resizeBehavior;
        }
        
        /**
         * Returns the insets.
         * 
         * @return insets
         */
        public Insets getInsets() {
            return (Insets)insets.clone();
        }
        
        /**
         * Get preferred width. Width is zero for resize behavior FILLED
         * @return
         */
        public int getPreferredWidth() {
        	return preferredWidth;
        }
    }
    
}
