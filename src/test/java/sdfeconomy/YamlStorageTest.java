/*
 */

package sdfeconomy;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sdfeconomy.storage.BankAccount;
import sdfeconomy.storage.PlayerAccount;
import sdfeconomy.storage.StorageCommitEveryUpdate;
import sdfeconomy.storage.yaml.YamlStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Observer;

import static org.junit.Assert.*;

/**
 *
 */
@RunWith(JUnit4.class)
public class YamlStorageTest
{
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private final String test_filename = "accounts_test.yml";

	@Test
	public void testWrite()
	{
		File out_file = new File(folder.getRoot(), test_filename);

		YamlStorage storage = new YamlStorage(out_file);
		storage.commit();

		assertTrue("Accounts file was not written", out_file.exists());
	}

	@Test
	public void testPlayerAccount()
	{
		File out_file = new File(folder.getRoot(), test_filename);

		// Use seperate scopes so underlying storage is deleted each time

		// Test creation of new player account
		{
			YamlStorage stor_save = new YamlStorage(out_file);
			PlayerAccount saved_account = stor_save.createPlayerAccount("Player1", "world1", 10.0);
			stor_save.commit();

			assertEquals("player1", saved_account.getName());
			assertEquals("world1", saved_account.getLocation());
			assertEquals(10.0, saved_account.getBalance(), 1e-6);
		}

		// Test can be read back in, turn save on update on to test
		{
			YamlStorage stor_read = new YamlStorage(out_file);
			stor_read.addObserver((Observer) new StorageCommitEveryUpdate());

			boolean has_account = stor_read.hasPlayerAccount("Player1", "world1");
			assertTrue("Player1 account was not created", has_account);

			// Make sure account names are case insensitive
			has_account = stor_read.hasPlayerAccount("PLAYER1", "WORLD1");
			assertTrue("Player1 account not found when using difference case", has_account);

			has_account = stor_read.hasPlayerAccount("Player2", "World2");
			assertFalse("Player2 account should not exist", has_account);

			PlayerAccount read_account = stor_read.getPlayerAccount("Player1", "world1");
			assertEquals("player1", read_account.getName());
			assertEquals("world1", read_account.getLocation());
			assertEquals(10.0, read_account.getBalance(), 1e-6);

			// Update to check observer updates
			read_account.setBalance(50.0);
		}

		// Check that updated balance was written
		{
			YamlStorage stor_update = new YamlStorage(out_file);
			stor_update.addObserver((Observer) new StorageCommitEveryUpdate());

			PlayerAccount updated_account = stor_update.getPlayerAccount("Player1", "world1");

			assertEquals(50.0, updated_account.getBalance(), 1e-6);

			stor_update.createPlayerAccount("Player2", "world1", 10.0);
			stor_update.createPlayerAccount("Player1", "world2", 10.0);
		}

		// Check that Player2 now exists
		{
			YamlStorage stor_update = new YamlStorage(out_file);
			stor_update.addObserver((Observer) new StorageCommitEveryUpdate());

			boolean has_account = stor_update.hasPlayerAccount("Player2", "world1");
			assertTrue("Player2 does not exist in world1", has_account);

			has_account = stor_update.hasPlayerAccount("Player1", "world2");
			assertTrue("Player1 does not exist in world2", has_account);

			ArrayList<String> names_expt = new ArrayList<String>();
			names_expt.add("player1");
			names_expt.add("player2");
			assertEquals(names_expt, stor_update.getPlayerNames("WORLD1"));

			// Check that we can delete Player2
			stor_update.deletePlayerAccount("Player2", "world1");
			has_account = stor_update.hasPlayerAccount("Player2", "world1");
			assertFalse("Player2 should not does not exist in world1 after delete", has_account);
		}
	}

	@Test
	public void testBankAccount()
	{
		File out_file = new File(folder.getRoot(), test_filename);

		// Use seperate scopes so underlying storage is deleted each time

		// Test creation of new bank account
		{
			YamlStorage stor_save = new YamlStorage(out_file);
			stor_save.addObserver((Observer) new StorageCommitEveryUpdate());

			BankAccount bank1 = stor_save.createBankAccount("bank1", "player1", "world1", 10.0);
			BankAccount bank2 = stor_save.createBankAccount("bank2", "player2", "world2", 15.0);

			assertTrue("bank1 was not created", stor_save.hasBankAccount("bank1"));
			assertTrue("bank1 not accessible using different case", stor_save.hasBankAccount("BANK1"));
			assertTrue("bank2 was not created", stor_save.hasBankAccount("bank2"));

			bank1.addMember("player3");
			bank2.addMember("player4");
		}

		// Test reading bank accounts and updating
		{
			YamlStorage stor_read = new YamlStorage(out_file);
			stor_read.addObserver((Observer) new StorageCommitEveryUpdate());

			BankAccount bank1 = stor_read.getBankAccount("bank1");
			BankAccount bank2 = stor_read.getBankAccount("bank2");

			ArrayList<String> names_expt = new ArrayList<String>();
			names_expt.add("bank1");
			names_expt.add("bank2");
			assertEquals(names_expt, stor_read.getBankNames());

			ArrayList<String> mem_expt1 = new ArrayList<String>();
			mem_expt1.add("player3");

			ArrayList<String> mem_expt2 = new ArrayList<String>();
			mem_expt2.add("player4");

			assertEquals("bank1", bank1.getName());
			assertEquals("world1", bank1.getLocation());
			assertEquals(10.0, bank1.getBalance(), 1e-6);
			assertEquals("player1", bank1.getOwner());
			assertTrue("player1 is the owner of bank1", bank1.isOwner("player1"));
			assertTrue("player3 is a member of bank1", bank1.isMember("player3"));
			assertEquals(mem_expt1, bank1.getMembers());

			assertEquals("bank2", bank2.getName());
			assertEquals("world2", bank2.getLocation());
			assertEquals(15.0, bank2.getBalance(), 1e-6);
			assertEquals("player2", bank2.getOwner());
			assertTrue("player2 is the owner of bank2", bank2.isOwner("PLAYER2"));
			assertTrue("player4 is a member of bank2", bank2.isMember("PLAyeR4"));
			assertEquals(mem_expt2, bank2.getMembers());

			// Remove a member and a bank, test save on update in next scope
			bank1.removeMember("player3");
			stor_read.deleteBankAccount("bank2");
		}

		// Test that member and bank removal worked
		{
			YamlStorage stor_read = new YamlStorage(out_file);

			BankAccount bank1 = stor_read.getBankAccount("bank1");

			assertFalse("player3 should not a member of bank1", bank1.isMember("player3"));
			assertFalse("bank2 should no longer exist", stor_read.hasBankAccount("bank2"));

			// Check that a null bank returns false
			assertFalse("null should not exist", stor_read.hasBankAccount(null));
		}
	}
}
