package org.jboss.tools.example.springmvc.data;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.tools.example.springmvc.controller.Cifras;
import org.jboss.tools.example.springmvc.model.Medicacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MedicacaoDao {
	
	@Autowired
	private EntityManager em;
	
	public Medicacao novaMedicacao(int numUtente, int idMedicamento, double dose, String indicacoes, String renovacao, int comprimidosPorCaixa) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		Medicacao Medicacao = new Medicacao(Cifras.encrypt(Integer.toString(numUtente)), idMedicamento, dose, indicacoes, renovacao, comprimidosPorCaixa);
		em.persist(Medicacao);
		return Medicacao;
	}
	
	public List<Medicacao> findAllByUtente(int numUtente) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		TypedQuery<Medicacao> query = em.createNamedQuery(Medicacao.FIND_ALL_BY_UTENTE, Medicacao.class);
		query.setParameter(Medicacao.UTENTE, Cifras.encrypt(Integer.toString(numUtente)));
		return query.getResultList();
	}
	
}
