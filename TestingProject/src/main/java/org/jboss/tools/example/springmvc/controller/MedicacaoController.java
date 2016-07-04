package org.jboss.tools.example.springmvc.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;

import org.jboss.tools.example.springmvc.data.InstituicaoDao;
import org.jboss.tools.example.springmvc.data.MedicacaoDao;
import org.jboss.tools.example.springmvc.data.MedicamentoDao;
import org.jboss.tools.example.springmvc.data.MedicamentoIdDao;
import org.jboss.tools.example.springmvc.data.MedicoDao;
import org.jboss.tools.example.springmvc.data.UtenteDao;
import org.jboss.tools.example.springmvc.model.Cirurgia;
import org.jboss.tools.example.springmvc.model.Medicacao;
import org.jboss.tools.example.springmvc.model.Medicamento;
import org.jboss.tools.example.springmvc.model.Medicamentoid;
import org.jboss.tools.example.springmvc.sensitivedata.Instituicao;
import org.jboss.tools.example.springmvc.sensitivedata.Medico;
import org.jboss.tools.example.springmvc.sensitivedata.Utente;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jboss.tools.example.springmvc.data.UtenteDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value="/medicacao")
public class MedicacaoController {

	@Autowired
	private UtenteDao utenteDao;
	
	
	@Autowired
	public MedicamentoDao medDao;
	
	@Autowired
	public UtenteDao utDao;
	
	@Autowired
	public InstituicaoDao instDao;
	
	@Autowired
	public MedicoDao medicoDao;
	
	@Autowired
	public MedicacaoDao medicacaoDao;
	
	@Autowired
	public MedicamentoIdDao medidDao;
	
	public HashMap<String,String> map = new HashMap<String, String>();

	
	@RequestMapping(value="/inserir", method = RequestMethod.POST,params={"nome", "dosagem", "indicacoes"})
	@ResponseBody
	public boolean inserirMedicacao(HttpSession session, @RequestParam(value="nome") String nomeMedicamento, @RequestParam(value="dosagem") double dosagemDiaria, @RequestParam(value="indicacoes") String indicacoes) throws InvalidKeyException, NumberFormatException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		System.out.println("cheguei aqui");
		System.out.println("tentei corresponder o medicamento:" + nomeMedicamento);
		Medicamentoid medid= medidDao.findByNome(nomeMedicamento);
		int id = medid.getID();
		Medicamento med = medDao.findById(id);
		System.out.println("medicamento id: " + med.getId());
		System.out.println("medicamento comprimidos: " + med.getComprimidos());
		if(medicacaoDao.exists(Integer.parseInt((String) session.getAttribute("sessionID")), med.getId())){
			return false;
		}
		else {
			medicacaoDao.novaMedicacao(Integer.parseInt((String) session.getAttribute("sessionID")), med.getId(), nomeMedicamento, dosagemDiaria, indicacoes, "Pendente", med.getComprimidos());
			return true;
		}
	}
	
	
	@RequestMapping(value="/obterMedicacoes")
	@ResponseBody
	public List<Medicacao> obterMedicacao(HttpSession session) throws InvalidKeyException, NumberFormatException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		List<Medicacao> lista = medicacaoDao.findAllByUtente(Integer.parseInt((String) session.getAttribute("sessionID"))); 
		return lista;
	}
	
	
	public boolean verifyLogin(HttpSession session) {
		System.out.println("A VERIFICAR SE ESTA LOGADO:");
		System.out.println("ID DE ACESSO:" + session.getAttribute("sessionID"));
		if(session.getAttribute("sessionID") == null){
			System.out.println("NAO TEM SESSAO");
			return false;
		}
		else{
			try {
				System.out.println("VERFICAR SE ESTA ACTIVA A CONTA");
				if(utenteDao.verifyActivatedUser((String)session.getAttribute("sessionID"))){System.out.println("VERFICAR SE ESTA ACTIVA A CONTA");
					return true;}
				else{
					System.out.println("NAO ESTA ACTIVA A CONTA");
					session.removeAttribute("sessionID");
				return false;
				}
			} catch (InvalidKeyException e) {
				
				e.printStackTrace();
			} catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				
				e.printStackTrace();
			} catch (BadPaddingException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		System.out.println("ERRO ESTRANHO");
		return false;
		
	}		
	
	
	@RequestMapping(value = "/view")
	public ModelAndView verificar(HttpSession session) throws InvalidKeyException, NumberFormatException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		ModelAndView mav = new ModelAndView();
		if(verifyLogin(session)){
			List<Medicacao> lista = medicacaoDao.findAllByUtente(Integer.parseInt((String) session.getAttribute("sessionID")));
			session.setAttribute("lista", lista);
			mav.addObject("lista", lista);
			mav.addObject("username", session.getAttribute("sessionName"));
			mav.setViewName("medicamentos");
		}
		else {
			mav.setViewName("redirect:/index");
		}
		return mav;
	}
	

	@RequestMapping(value="/verReceita", method = RequestMethod.POST, params={"medicacaoID"})
	@ResponseBody
	public ModelAndView verReceita(HttpSession session, @RequestParam("id") String id) throws NumberFormatException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("receita");
		Utente ut = utDao.findUtenteById(Integer.parseInt((String) session.getAttribute("sessionID")));
		mav.addObject("utenteName", ut.getNome());
		mav.addObject("utenteID", ut.getNumUtente());
		mav.addObject("utenteTelemovel", ut.getTelemovel());
		int idMedicacao = Integer.parseInt(id);
		Medicacao med = medicacaoDao.findById(idMedicacao);
		mav.addObject("nomeMedicamento", med.getNomeMedicamento());
		double dose = med.getDose();
		mav.addObject("dose", dose);
		if (dose == 1) {
			mav.addObject("extenso", "um");
		}
		else if (dose == 2) {
			mav.addObject("extenso", "dois");
		}
		else if (dose == 3) {
			mav.addObject("extenso", "três");
		}
		Instituicao inst = instDao.findById(ut.getCentroSaude());
		mav.addObject("medicacaoID", med.getId());
		mav.addObject("instituicao", inst.getNome());
		Medico medico = medicoDao.findById(ut.getMedico());
		mav.addObject("nomeMedico", medico.getNome());
		mav.addObject("contactoInstituicao", inst.getTelefone());
		mav.addObject("data", new Date());
		return mav;
	}
	
	
	@RequestMapping(value = "/renovar", method = RequestMethod.POST,params={"id"})
	@ResponseBody
	public boolean renovarMed(HttpSession session, @RequestParam("id") String id) {
		System.out.println("im here");
		return medicacaoDao.renovarMed(Integer.parseInt(id));
	}
	
	@RequestMapping(value = "/apagar", method = RequestMethod.POST, params={"id"})
	@ResponseBody
	public boolean apagarMed(HttpSession session, @RequestParam("id") String id) {
		return medicacaoDao.deleteMedicacao(Integer.parseInt(id));
	}
	



}
