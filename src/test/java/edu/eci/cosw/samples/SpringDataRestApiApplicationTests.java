package edu.eci.cosw.samples;


import edu.eci.cosw.example.persistence.PatientsRepository;
import edu.eci.cosw.jpa.sample.model.Consulta;
import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.samples.services.PatientServices;
import edu.eci.cosw.samples.services.ServicesException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataRestApiApplication.class)
@WebAppConfiguration

@ActiveProfiles("test")
public class SpringDataRestApiApplicationTests {

        
@Autowired
    private PatientsRepository patientrepo;

    @Autowired
    private PatientServices patientserv;

    @Test
	public void existeUnPaciente(){

        PacienteId pId = new PacienteId(1023942826, "cc");
        Paciente paciente = new Paciente(pId, "kristhian camilo gomez", new Date());
        patientrepo.save(paciente);
        
        try {
            Paciente pacienteConsulta = patientserv.getPatient(1023942826, "cc");
            Assert.assertEquals(pacienteConsulta.getId().getId()+pacienteConsulta.getId().getTipoId(),paciente.getId().getId()+paciente.getId().getTipoId());
        } catch (ServicesException ex) {
            Logger.getLogger(SpringDataRestApiApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Existe el paciente");
        }
    }

    @Test
    public void noExistePaciente(){

        patientrepo.deleteAll();
        PacienteId pacienteId=new PacienteId(1023942826,"cc");
        Paciente paciente = new Paciente(pacienteId, "kristhian camilo gomez", new Date(1990,01,01));
        Paciente pacientePru=patientrepo.findOne(pacienteId);
        Assert.assertNull("No existe",pacientePru);
    }

    @Test
    public void noExistePacientesConNConsultas() throws ServicesException {

        patientrepo.deleteAll();
        PacienteId pacienteId=new PacienteId(1023942826,"cc");
        Paciente paciente = new Paciente(pacienteId, "kristhian camilo gomez", new Date(2000,10,05));
        paciente.getConsultas().add(new Consulta(new Date(2012,5,21), "Sueño constante"));
        patientrepo.save(paciente);
        List<Paciente> pacientes = patientserv.topPatients(2);
        Assert.assertEquals("Lista vacia",pacientes.size(),0);

    }

    @Test
    public void existePacientesConNConsultas() throws ServicesException {

        patientrepo.deleteAll();
        PacienteId pacienteId=new PacienteId(1023942826,"cc");
        Paciente paciente = new Paciente(pacienteId, "kristhian gomez", new Date(1990,01,01));       
        
        PacienteId pacienteId2=new PacienteId(123400,"cc");
        Paciente paciente2 = new Paciente(pacienteId2, "Angelica ortiz", new Date(2010,11,12));        
        
        PacienteId pacienteId3=new PacienteId(567890,"cc");
        Paciente paciente3 = new Paciente(pacienteId3, "Carlitos Gonzales", new Date(2001,10,12));
         
        

        patientrepo.save(paciente);
        
        
        paciente2.getConsultas().add(new Consulta(new Date(2010,15,20), "Sintomas de dolor"));
        patientrepo.save(paciente2);

        paciente3.getConsultas().add(new Consulta(new Date(2000,5,16), "Pecho"));
        paciente3.getConsultas().add(new Consulta(new Date(2005,19,17), "Espalda"));
        patientrepo.save(paciente3);

        List<Paciente> pacientes = patientserv.topPatients(1);

        Assert.assertEquals("Lista",pacientes.size(),2);
    }

}
