package com.modes;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import com.MainMenu;
import com.components.ImageBlock;
import com.utils.ImageManager;
import com.utils.Utils;

import java.awt.Image;

@SuppressWarnings("serial")
public class SlideShow extends WindowInterface {
	
	private BufferedImage image;
	private ImageBlock panelImage;
	private int id;
	public int imageId = 1;
	private boolean running;
	public boolean updating = false;
	private long waitTime;
	private long imageTime;
	private ArrayList<String> images = new ArrayList<String>();
	
	/**
	 * Create the application.
	 */
	public SlideShow(MainMenu menu) {
		super(menu);
		path = ImageManager.saveLocation + "\\raw\\";
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void start(GraphicsConfiguration graphicsConfiguration) {
		super.start(graphicsConfiguration);
		running = true;
		//frmSlide = new JFrame();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		
		panelImage = new ImageBlock();
		/*panelImage = new ImageBlock() {

			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        if (image != null) {
		        	BufferedImage sprite = Utils.copyImage(Utils.resize((BufferedImage) image, getWidth(), getHeight()));
		        	g.drawImage(sprite, 0, 0, this);
		        }
		    }
		};*/
		panelImage.setBackground(Utils.backgroundColor);
		getContentPane().add(panelImage, BorderLayout.CENTER);
		panelImage.setLayout(null);
		
		JPanel panel = new JPanel() {

			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
				try {
					Image logo = ImageIO.read(MainMenu.class.getResource("images/logo2.png"));
			        BufferedImage sprite = Utils.copyImage(Utils.resize((BufferedImage) logo, getWidth(), getHeight()));
			        g.drawImage(sprite, 0, 0, this);
				} catch (IOException e) {
				}
		    }
		};
		panel.setOpaque(false);
		panel.setBounds(10, 11, 88, 33);
		panelImage.add(panel);
		double frmW = getWidth();
		double frmH = getHeight();
		setVisible(true);
		double width = (double) panel.getWidth() * (100.0 / frmW * (double) getWidth() / 100.0);
		double height = (double) panel.getHeight() * (100.0 / frmH * (double) getHeight() / 100.0);
		panel.setSize((int) width , (int) height);
		panel.setLocation(10, getHeight() - 10 - panel.getHeight());
		ImageThread thread = new ImageThread();
		thread.start();
	}

	public void setID(int id) {
		this.id = id;
	}
	
	public void loadImages() {
		images.clear();
		File directory = new File(path);
        File[] fileList = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
            	name = name.toLowerCase();
                return name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".bmp") || name.endsWith(".raw");
            }
        });
        for (File file : fileList) {
        	images.add(file.getName());
        	//System.out.println(file.getName());
        }
	}
	
	public boolean updateImages() {
		if (needsUpdate()) {
			updating = true;
			loadImages();
			updating = false;
			return true;
		}
		return false;
	}
	
	public boolean needsUpdate() {
		int size = new File(path).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
            	name = name.toLowerCase();
            	return name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".bmp") || name.endsWith(".raw");
            }
        }).length;
		if (size != images.size())
			return true;
		return false;
	}
	
	public int getImageId(String location) {
		String name = location + "\\IMG_" + String.format("%04d", imageId) + ".jpg";
		//System.out.println(name);
		File file = new File(name);
		if (file.exists()) {
			imageId++;
			return getImageId(location);
		}
		/*File dir = new File(location);
		int id = 0;
	    for (File f : dir.listFiles()) {
	    	
	        String name = f.getName();
	        name = name.replaceAll("IMG_", "");
	        System.out.println(name);
	    }*/
		imageId++;
		return imageId - 1;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	class ImageThread extends Thread {
		@Override
		public void run() {
			waitTime = System.currentTimeMillis();
			imageTime = System.currentTimeMillis();
			if (updateImages()) {
				id = images.size();
				waitTime = waitTime - 5000;
			}
			while(running) {
				if (System.currentTimeMillis() - imageTime > 2000) {
					if (updateImages()) {
						id = images.size();
						waitTime = waitTime - 5000;
					}
					imageTime = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() - waitTime > 5000 && !updating) {
					int size = images.size();
					id--;
					if (id <= 0)
						id = size - 1;
					else if (id >= size)
						id = 0;
					if (size > 0) {
						try {
							image = ImageIO.read(new File(path + "\\" + images.get(id)));
							//image = ImageIO.read(new File(ImageManager.saveLocation + "/" + ImageManager.getImages().get(id)));
							
							//panelImage.setImage((new File(path + "\\" + images.get(id)).toURI().toURL());
							panelImage.setImage(image);
						} catch (IOException e) {
						}
					}
					waitTime = System.currentTimeMillis();
				}
			}
		}
	}
}
