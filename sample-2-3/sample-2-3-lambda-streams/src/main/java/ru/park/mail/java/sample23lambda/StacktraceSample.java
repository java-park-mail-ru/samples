package ru.park.mail.java.sample23lambda;

public class StacktraceSample {

	public static void main(String[] args) {
		new ManagerFactory().create().manage();
	}

	public static class Developer {
		public void develop() {
			throw new IllegalStateException("I hate enterprise!");
		}
	}

	public static class DeveloperFactory {
		public Developer create() {
			try {
				Developer dev = new Developer();
				dev.develop();
				return dev;
			} catch (Exception e) {
				throw new IllegalStateException("We are short of developers!", e);
			}
		}
	}

	public static class Manager {
		private final DeveloperFactory developerFactory;

		public Manager(DeveloperFactory developerFactory) {
			this.developerFactory = developerFactory;
		}

		public void manage() {
			developerFactory.create().develop();
		}
	}

	public static class ManagerFactory {
		public Manager create() {
			try {
				Manager manager = new Manager(new DeveloperFactory());
				manager.manage();
				return manager;
			} catch (Exception e) {
				throw new IllegalStateException("Managers do nothing and still we cannot create one!", e);
			}
		}
	}

}
