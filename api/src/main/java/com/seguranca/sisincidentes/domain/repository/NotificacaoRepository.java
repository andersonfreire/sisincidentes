package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.Notificacao;
import com.seguranca.sisincidentes.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByDestinatarioOrderByDataCriacaoDesc(Usuario destinatario);

    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.destinatario.id = :usuarioId AND n.lida = false")
    long countNaoLidasByUsuarioId(@Param("usuarioId") Long usuarioId);
}
