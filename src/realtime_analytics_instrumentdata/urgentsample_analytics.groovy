package realtime_analytics_instrumentdata

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.drools.core.command.impl.GenericCommand;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient

import com.kiin.ui.ruleprocessor.ConfigUtil;
import com.kiin.ui.ruleprocessor.FileContent;

split = payload.tokenize("\t");
FileContent fileContent = new FileContent();
String url = ConfigUtil.getPropertyByName("SRVR_URL");
String username = ConfigUtil.getPropertyByName("USERNAME");
String password = ConfigUtil.getPropertyByName("PASSWORD");
String container = ConfigUtil.getPropertyByName("CONTAINER");

	fileContent.setSite_producer_name(split[0]);
	fileContent.setSite_sender_name(split[1]);
	fileContent.setOrder_number(split[2]);
	fileContent.setOrderer_code(split[3]);
	fileContent.setOrderer_group(split[4]);
	fileContent.setSample_is_urgent(split[5]);
	fileContent.setEvent_datetime(split[26]);
	fileContent.setOrderer_functional_unit("");
	fileContent.setSample_id("");
	fileContent.setSample_subtype("");
	fileContent.setSample_rank("");
	fileContent.setSample_is_real("");
	fileContent.setSample_is_aliquot("");
	fileContent.setSample_aliquot_parent_id("");
	fileContent.setSample_type_connection_instrument_code("");
	fileContent.setSample_type("");
	fileContent.setSample_type_label("");
	fileContent.setTube_preanalytical_mpl_link_code("");
	fileContent.setTube_preanalytical_mpl_workcycle("");
	fileContent.setTube_preanalytical_mpl_workplace("");
	fileContent.setParameter_name("");
	fileContent.setParameter_with_sample_type_name("");
	fileContent.setParameter_instrument_connection_code("");
	fileContent.setParameter_lis_connection_code("");
	fileContent.setParameter_discipline_name("");
	fileContent.setParameter_type("");
	fileContent.setEvent_id("");
	fileContent.setEvent_actor("");
	fileContent.setInstrument_name("");
	fileContent.setInstrument_serial_number("");
	fileContent.setInstrument_mpl_number("");
	fileContent.setInstrument_model("");
	fileContent.setInstrument_parent_name("");
	fileContent.setInstrument_connection_driver_type("");
	fileContent.setLis_connection_code("");
	fileContent.setParameter_dimension_1("");
	fileContent.setParameter_dimension_2("");
	fileContent.setParameter_dimension_3("");
	fileContent.setParameter_dimension_4("");
	fileContent.setParameter_dimension_5("");
	fileContent.setInstrument_dimension_1("");
	fileContent.setInstrument_dimension_2("");
	fileContent.setExportation_realisation_date("");
	fileContent.setExportation_date("");
	fileContent.setCustomer_licence_name("");
	fileContent.setMiddleware_network_id("");
	fileContent.setCustomer_contract_isppm("");
	fileContent.setFile_name("");

	
	KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(url, username, password);
	//Set<Class<?>> allClasses = new HashSet<Class<?>>();
	//allClasses.add(UploadedFile.class);
	//config.addJaxbClasses(allClasses);
	config.setMarshallingFormat(MarshallingFormat.XSTREAM);
	

	KieServicesClient client  = KieServicesFactory.newKieServicesClient(config);
	RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);
	
	List<GenericCommand<?>> commands = new ArrayList<GenericCommand<?>>();
	
	commands.add((GenericCommand<?>) KieServices.Factory.get().getCommands().newInsert(fileContent,"InputData"));
	commands.add((GenericCommand<?>) KieServices.Factory.get().getCommands().newFireAllRules("fire-identifier"));
	BatchExecutionCommand batchCommand = KieServices.Factory.get().getCommands().newBatchExecution(commands);
	ServiceResponse<String> kieresponse = ruleClient.executeCommands(container, batchCommand);
	//System.out.println(kieresponse.getMsg());
	
	
	String output = kieresponse.getResult();
	
	//System.out.println("output --> "+output);
	
	JAXBContext jaxbContext = JAXBContext.newInstance(FileContent.class);
	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	
	String textFrom = "<result identifier=\"InputData\">";
	String textTo = "</result>";
	String result =
			output.substring(
					output.indexOf(textFrom) + textFrom.length(),
					output.length());
	
	result =
			  result.substring(
				0,
				result.indexOf(textTo));
	
	CharSequence cs1 = "__";
	if (result.contains(cs1))
		result = result.replaceAll("__", "_");
	
	//System.out.println("result -->"+result);
	
	FileContent fileOut = (FileContent)jaxbUnmarshaller.unmarshal(new StringReader(result));
	
	if (fileOut.isDq1_passed() )
	{
		
		System.out.println(fileOut.isDq1_passed() );
		fileContent = null;
		
	}
    
	String returVal = "DQ1 status is "+ fileOut.isDq1_passed().toString()+" and the site sender is "+fileOut.getSite_producer_name();  	
  
return returVal;
