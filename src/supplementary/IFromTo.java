package supplementary;

import java.awt.Component;

import devices.Monitor;

public interface IFromTo {
	/**
	* Метод обробки "події" - початок руху транзакції.
	* Викликається перед перед початком руху mn.
	* @param mn транзакція, що виходить з об’єкта
	*/
	public void onOut(Monitor mn);

	/**
	 * Метод обробки "події" - кінець руху транзакції.
	 * Викликається, коли mn приходить к об’єкту. 
	 * @param mn транзакція,що прийшла до об’єкта
	 */
	public void onIn(Monitor mn);

	/**
	 * Метод, що надає доступ до компонента, який представляє об'єкт на GUI
	 * @return посилання на компонент, який відображає об'єкт на GUI
	 */
	public Component getComponent();

}
