package it.unical.googlecalendar.tests;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import it.unical.googlecalendar.configuration.AppConfiguration;
import it.unical.googlecalendar.dao.MemoDAO;
import it.unical.googlecalendar.dao.UserDAOImpl;
import it.unical.googlecalendar.model.Memo;
import it.unical.googlecalendar.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class MemoTest {
	@Autowired
	private MemoDAO mdao;
	@Autowired
	private UserDAOImpl udao;

	@Test
	public void saveTest() {

		User pippo = new User("pippo88@p.it", "pippo", "1234");
		udao.save(pippo);
		Memo m = new Memo(pippo, "Memo1", new Date(), "prova", Color.BLACK, Color.cyan);
		Memo m1 = new Memo(pippo, "Memo2", new Date(), "prova2", Color.BLACK, Color.cyan);
		mdao.save(m1);
		mdao.save(m);

		List<Memo> list = pippo.getMemos();
		//System.out.println("Size memos di pippo" + list.size());
//		for (Memo mem : list) {
//			System.out.println(mem.getTitle() + " " + m.getUser().getUsername());
//		}
	}

}
