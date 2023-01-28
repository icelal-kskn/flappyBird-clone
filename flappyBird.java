package flappyBird;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JPanel;
public class flappyBird implements ActionListener, MouseListener, KeyListener
{

	public static flappyBird flappyBird;

	public final int OYUN_X_BOYU = 800, OYUN_Y_BOYU = 800;

	public Renderer karıştırıcı;

	public Rectangle kuş;

	public ArrayList<Rectangle> duvar;

	public int fareTık, yHareket, skor;

	public boolean oyunBittiMi, başladıMı;

	public Random rasgele;

	public flappyBird()
	{
		JFrame jframe = new JFrame();
		Timer sayaç = new Timer(20, this);

		karıştırıcı = new Renderer();
		rasgele = new Random();

		jframe.add(karıştırıcı);
		jframe.setTitle("Flappy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(OYUN_X_BOYU, OYUN_Y_BOYU);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		kuş = new Rectangle(OYUN_X_BOYU / 2 - 10, OYUN_Y_BOYU / 2 - 10, 20, 20);
		duvar = new ArrayList<Rectangle>();

		duvarEkle(true);
		duvarEkle(true);
		duvarEkle(true);
		duvarEkle(true);

		sayaç.start();
	}

	public void duvarEkle(boolean başla)
	{
		int space = 300;
		int width = 100;
		int height = 50 + rasgele.nextInt(300);

		if (başla)
		{
			duvar.add(new Rectangle(OYUN_X_BOYU + width + duvar.size() * 300, OYUN_Y_BOYU - height - 120, width, height));
			duvar.add(new Rectangle(OYUN_X_BOYU + width + (duvar.size() - 1) * 300, 0, width, OYUN_Y_BOYU - height - space));
		}
		else
		{
			duvar.add(new Rectangle(duvar.get(duvar.size() - 1).x + 600, OYUN_Y_BOYU - height - 120, width, height));
			duvar.add(new Rectangle(duvar.get(duvar.size() - 1).x, 0, width, OYUN_Y_BOYU - height - space));
		}
	}

	public void paintColumn(Graphics g, Rectangle column)
	{
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	public void hopla()
	{
		if (oyunBittiMi)
		{
			kuş = new Rectangle(OYUN_X_BOYU / 2 - 10, OYUN_Y_BOYU / 2 - 10, 20, 20);
			duvar.clear();
			yHareket = 0;
			skor = 0;

			duvarEkle(true);
			duvarEkle(true);
			duvarEkle(true);
			duvarEkle(true);

			oyunBittiMi = false;
		}

		if (!başladıMı)
		{
			başladıMı = true;
		}
		else if (!oyunBittiMi)
		{
			if (yHareket > 0)
			{
				yHareket = 0;
			}

			yHareket -= 10;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int hız = 10;

		fareTık++;

		if (başladıMı)
		{
			for (int i = 0; i < duvar.size(); i++)
			{
				Rectangle column = duvar.get(i);

				column.x -= hız;
			}

			if (fareTık % 2 == 0 && yHareket < 15)
			{
				yHareket += 2;
			}

			for (int i = 0; i < duvar.size(); i++)
			{
				Rectangle column = duvar.get(i);

				if (column.x + column.width < 0)
				{
					duvar.remove(column);

					if (column.y == 0)
					{
						duvarEkle(false);
					}
				}
			}

			kuş.y += yHareket;

			for (Rectangle column : duvar)
			{
				if (column.y == 0 && kuş.x + kuş.width / 2 > column.x + column.width / 2 - 10 && kuş.x + kuş.width / 2 < column.x + column.width / 2 + 10)
				{
					skor++;
				}

				if (column.intersects(kuş))
				{
					oyunBittiMi = true;

					if (kuş.x <= column.x)
					{
						kuş.x = column.x - kuş.width;

					}
					else
					{
						if (column.y != 0)
						{
							kuş.y = column.y - kuş.height;
						}
						else if (kuş.y < column.height)
						{
							kuş.y = column.height;
						}
					}
				}
			}

			if (kuş.y > OYUN_Y_BOYU - 120 || kuş.y < 0)
			{
				oyunBittiMi = true;
				
			}

			if (kuş.y + yHareket >= OYUN_Y_BOYU - 120)
			{

				kuş.y = OYUN_Y_BOYU - 120 - kuş.height;
				oyunBittiMi = true;
			}
		}

		karıştırıcı.repaint();
	}

	public void repaint(Graphics g)
	{
		g.setColor(Color.cyan);
		g.fillRect(0, 0, OYUN_X_BOYU, OYUN_Y_BOYU);

		g.setColor(Color.orange);
		g.fillRect(0, OYUN_Y_BOYU - 120, OYUN_X_BOYU, 120);

		g.setColor(Color.green);
		g.fillRect(0, OYUN_Y_BOYU - 120, OYUN_X_BOYU, 20);

		g.setColor(Color.red);
		g.fillRect(kuş.x, kuş.y, kuş.width, kuş.height);

		for (Rectangle column : duvar)
		{
			paintColumn(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Times New Roman", 1, 100));

		if (!başladıMı)
		{
			g.drawString("Click to start!", 75, OYUN_Y_BOYU / 2 - 50);
		}

		if (oyunBittiMi)
		{
			g.drawString("Game Over!", 100, OYUN_Y_BOYU / 2 - 50);
			g.drawString("Score: "+String.valueOf(skor), 200, OYUN_Y_BOYU/2 + 55);
		}

		if (!oyunBittiMi && başladıMı)
		{
			g.drawString(String.valueOf(skor), OYUN_X_BOYU / 2 - 25, 100);
		}
	}

	public static void main(String[] args)
	{
		flappyBird = new flappyBird();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		hopla();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			hopla();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}

    public class Renderer extends JPanel
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		flappyBird.repaint(g);
	}	
}
}