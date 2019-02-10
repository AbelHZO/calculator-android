package com.abelhzo.calculator.services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @autor: Abel_HZO
 * @company: AbelHZO
 * @created: 11/11/2018 13:29:23
 * @file: CalculationServiceImpl.java
 * @license: <i>GNU General Public License<i>
 *
 */
public class CalculationServiceImpl implements CalculationService {

	public Double calculation(String cad) {
		Double result = 0.0;
		
		String cadena = deleteFirstAndLastChar(cad);
		List<String> listOfStrings = segmentString(cadena);
		result = multiplicationAndDivision(listOfStrings);
		result += addAndSubstract(listOfStrings);
		
		return result;
	}
	
	private String deleteFirstAndLastChar(String cad) {
		String cadTemp = "";
		boolean truncate = false;
		/**
		 * En caso de que la cadena termine con un operador lo quitara de la cadena.
		 */
		for(int i = cad.length(); i > 0 ; i--) {
			String lastCharacter = cad.substring(i - 1, i);
			if(lastCharacter.matches("(\\+|-|x|/|\\.)")) {
				cadTemp = cad.substring(0, i - 1);
				truncate = true;
			} else {
				break;
			}	
		}
		
		if(truncate)
			cad = cadTemp;
		
		truncate = false;
		
		/**
		 * En caso de comenzar con signo de multiplicación o división, lo elimina.
		 */
		
		for(int i = 0; i < cad.length(); i++) {
			String firstCharacter = cad.substring(i, i + 1);
			if(firstCharacter.matches("(x|/|\\+)")) {
				cadTemp = cad.substring(i + 1, cad.length());
				truncate = true;
			} else {
				break;
			}
		}
		
		if(truncate)
			cad = cadTemp;
		
		return cad;
	}
	
	private List<String> segmentString(String cad) {
		
		char arrayCad[] = cad.toCharArray();
		
		List<String> listOfStrings = new ArrayList<String>();
		
		String digit = "";     //Variable para concatenar los digitos

		for(int j = 0; j < arrayCad.length; j++) {      //Itera el arreglo
			if(String.valueOf(arrayCad[j]).trim().matches("(\\d+|\\.)")) {  //Si es numero o punto
				digit += arrayCad[j];                                       //Se concatena a la variable
			} else if(String.valueOf(arrayCad[j]).trim().matches("(\\+|-|x|/)")) { //Si es signo
				if(!digit.trim().equals("")) {                                           //Y la concatenacion NO esta vacia
					listOfStrings.add(digit.trim());                                     //Agrega numero a la lista
					digit = "";                                                          //Resetea la variable concatenada 
				}
				listOfStrings.add(String.valueOf(arrayCad[j]).trim());                   //Agrega signo a la lista
			} else {                                                                     //Si es registro vacio
				if(!digit.trim().equals("")) {                                           //Y la concatenacion NO esta vacia
					listOfStrings.add(digit.trim());                                     //Agrega numero a la lista
					digit = "";                                                          //Resetea la variable concatenada
				}
			}
		}
				
		if(!digit.trim().equals(""))                            //Si la concatenacion NO esta vacia
			listOfStrings.add(digit.trim());					//Agrega numero concatenado a la lista

		
		return listOfStrings;
	}
	
	private Double multiplicationAndDivision(List<String> listOfStrings) {
		
		Double resp = 0.0;              //Respuesta definitiva de multiplicaciones y divisiones
		Double respTemp = 0.0;			//Respuesta temporal por cada seccion de multiplicaciones o divisiones
		String operatorBefore = ""; 	//Almacena signo (- | +)	
		
		try {
		
			for(int i = 0; i < listOfStrings.size(); i++) {				//Recorre la lista creada en el modulo anterior                                                                                                                  
				if(listOfStrings.get(i).equals("x") && i != listOfStrings.size() - 1) {                  //Si (i) apunta a operador multiplicación
					if(respTemp == 0 && listOfStrings.get(i + 1).matches("(\\d+|\\d+\\.\\d+|\\.\\d+)"))	 //Ejecuta caso cuando (i) apunta al primer '*': |5|-|3.3|*|8|*|7|
						respTemp = Double.parseDouble(operatorBefore + listOfStrings.get(i - 1)) * Double.parseDouble(listOfStrings.get(i + 1));
					else if(respTemp == 0 && listOfStrings.get(i + 1).matches("(\\+|-)")) 	 //Ejecuta caso cuando (i) apunta al primer '*': |5|-|3.3|*|-|8|*|7|
						respTemp = Double.parseDouble(operatorBefore + listOfStrings.get(i - 1)) * Double.parseDouble(listOfStrings.get(i + 1) + listOfStrings.get(i + 2));
					else if(listOfStrings.get(i + 1).matches("(\\+|-)"))						 //Ejecuta caso cuando (i) apunta al segundo '*': |5|-|3.3|*|8|*|-|7|
						respTemp *= Double.parseDouble(listOfStrings.get(i + 1) + listOfStrings.get(i + 2));
					else            															 //Ejecuta caso cuando (i) apunta al segundo y tercer '*' : |5|-|3.3|*|8|*|7|*|3|                                  
						respTemp *= Double.parseDouble(listOfStrings.get(i + 1));   					
				} else if(listOfStrings.get(i).equals("/") && i != listOfStrings.size() - 1) {           //Si (i) apunta a operador división
					if(respTemp == 0 && listOfStrings.get(i + 1).matches("(\\d+|\\d+\\.\\d+|\\.\\d+)"))  //Ejecuta caso cuando (i) apunta al primer '/': |5|-|3.3|/|8|/|7|
						respTemp = Double.parseDouble(operatorBefore + listOfStrings.get(i - 1)) / Double.parseDouble(listOfStrings.get(i + 1));
					else if(respTemp == 0 && listOfStrings.get(i + 1).matches("(\\+|-)"))      //Ejecuta caso cuando (i) apunta al primer '/': |5|-|3.3|/|-|8|/|7|
						respTemp = Double.parseDouble(operatorBefore + listOfStrings.get(i - 1)) / Double.parseDouble(listOfStrings.get(i + 1) + listOfStrings.get(i + 2));
					else if(listOfStrings.get(i + 1).matches("(\\+|-)"))					     //Ejecuta caso cuando (i) apunta al segundo '/': |5|-|3.3|/|8|/|-|7|
						respTemp /= Double.parseDouble(listOfStrings.get(i + 1) + listOfStrings.get(i + 2));
					else																		 //Ejecuta caso cuando (i) apunta al segundo y tercer '/' : |5|-|3.3|/|8|/|7|/|3|
						respTemp /= Double.parseDouble(listOfStrings.get(i + 1));		
				}
				
				//Si (i) apunta a (+ | -) & (i == 0 | El registro anterior no es (* | /)) :: Actualiza signoAnterior
				if((listOfStrings.get(i).equals("+") || listOfStrings.get(i).equals("-")) && ((i == 0 || !listOfStrings.get(i - 1).matches("(x|/)")))) {
					operatorBefore = listOfStrings.get(i);
					if(respTemp != 0) {   //Si la respuesta temporal no es 0
						resp += respTemp; //Sumalo a resp 
						respTemp = 0.0;   //Resetea la respuesta temporal
					}
				}
			}
		
		} catch(NumberFormatException nfe) {
			respTemp = 0.0;
		}
		
		if(respTemp != 0) resp += respTemp;    //Adicionar la ultima respuesta temporal a respuesta definitiva. 
		
		return resp;
	}
	
	private Double addAndSubstract(List<String> listOfStrings) {
		
		Double resp = 0.0;
		for(int i = 0; i < listOfStrings.size() && listOfStrings.size() > 2; i++) {
			if(listOfStrings.get(i).matches("(\\d+|\\d+\\.\\d+|\\.\\d+)")) {  //Si es numero entero o deciamal
				if((i == 0 || i == 1) && listOfStrings.get(i + 1).matches("(x|/)")) {
					continue;   //Evita caso cuando (i) apunta a 6.3: |-|6.3|*|5| o |6.3|*|5|
			    } else if((i == 0 || i == 1) && listOfStrings.get(i + 1).matches("(\\+|-)")) {
			    	if(i == 0) resp += Double.parseDouble(listOfStrings.get(i));   //Ejecuta caso cuando (i) apunta a 3.8:  |3.8|+|4|
			    	if(i == 1) resp += Double.parseDouble(listOfStrings.get(i - 1) + listOfStrings.get(i));  //Ejecuta caso cuando (i) apunta a 3.8:  |-|3.8|+|4|
			    } else if(i >= listOfStrings.size() - 1 && listOfStrings.get(i - 1).matches("(\\+|-)") && !listOfStrings.get(i - 2).matches("(x|/)")) {
			    	resp += Double.parseDouble(listOfStrings.get(i - 1) + listOfStrings.get(i));  //Ejecuta caso cuando (i) apunta a 3: |2.5|*|2|-|3|, y evita caso (i) apuntando a 3: |2.5|*|2|*|-|3|
			    } else if(i >= listOfStrings.size() - 1 && (listOfStrings.get(i - 1).matches("(x|/)") || listOfStrings.get(i - 2).matches("(x|/)")))	{
			    	continue;   //Evita caso cuando (i) apunta a 8: |5.2|+|3|*|8|  y |5.2|+|3|*|-|8|
				} else if(listOfStrings.get(i - 1).matches("(\\+|-)") && listOfStrings.get(i + 1).matches("(\\+|-)") && !listOfStrings.get(i - 2).matches("(x|/)")) {
					resp += Double.parseDouble(listOfStrings.get(i - 1) + listOfStrings.get(i));  //Ejecuta caso cuando (i) apunta a 9.3: |8|-|9.3|+|5|, y evita caso (i) apuntando a 9.3: |8|*|-|9.3|+|5|
				}
			}
		}
		
		if(resp == 0.0) {
			if(listOfStrings.size() == 1) 
				resp = Double.parseDouble(listOfStrings.get(0));
			else if(listOfStrings.size() == 2)
				resp = Double.parseDouble(listOfStrings.get(0) + listOfStrings.get(1));
		}
		
		return resp;
	}

}