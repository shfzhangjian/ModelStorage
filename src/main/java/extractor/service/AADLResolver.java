package extractor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import extractor.DAO.mapper.InvocationChannelMapper;
import extractor.DAO.mapper.MessageChannelMapper;
import extractor.DAO.mapper._eventMapper;
import extractor.DAO.mapper._exceptionMapper;
import extractor.DAO.mapper._partitionMapper;
import extractor.DAO.mapper._provideMapper;
import extractor.DAO.mapper._requireMapper;
import extractor.DAO.mapper._stateMapper;
import extractor.DAO.mapper._taskMapper;
import extractor.DAO.mapper.busMapper;
import extractor.DAO.mapper.communicationchannelMapper;
import extractor.DAO.mapper.componentMapper;
import extractor.DAO.mapper.componenttransitionMapper;
import extractor.DAO.mapper.connectionsMapper;
import extractor.DAO.mapper.dataobjectMapper;
import extractor.DAO.mapper.deviceMapper;
import extractor.DAO.mapper.errorpathMapper;
import extractor.DAO.mapper.linkpointMapper;
import extractor.DAO.mapper.propagationMapper;
import extractor.DAO.mapper.rtosMapper;
import extractor.DAO.mapper.taskscheduleMapper;
import extractor.DAO.mapper.transitionMapper;
import extractor.DAO.mapper.transitionstateMapper;
import extractor.model.ASyncMessaging;
import extractor.model.DispatchChannel;
import extractor.model.InvocationChannel;
import extractor.model.MessageChannel;
import extractor.model._event;
import extractor.model._exception;
import extractor.model._partition;
import extractor.model._provide;
import extractor.model._require;
import extractor.model._state;
import extractor.model._task;
import extractor.model.bus;
import extractor.model.communicationchannel;
import extractor.model.component;
import extractor.model.componenttransition;
import extractor.model.connections;
import extractor.model.dataobject;
import extractor.model.device;
import extractor.model.errorpath;
import extractor.model.linkpoint;
import extractor.model.propagation;
import extractor.model.rtos;
import extractor.model.shareddataaccess;
import extractor.model.syncinterface;
import extractor.model.taskschedule;
import extractor.model.transition;
import extractor.model.transitionstate;
import extractor.util.AppendID;
import extractor.util.GetID;

//????????????,???????????????????????????????????????
@Service("AADLResolver")
public class AADLResolver {
//	@Autowired
//	private ModelService ms;
	// private ModelService ms;
	// ????????????????????????????????????
	Map<String, String> aadlFiles = new HashMap<String, String>();
	static String modeldirectory;
	static String dynamicfilename;

	static String hardmodelfile;
	static String errlibfile;
	static String compositelibfile;

	static List<? extends Node> components = null;
	static List<? extends Node> cchannels = null;
	static Map<String, component> componentlist = new HashMap<String, component>();
	static Map<String, component> taskcomponentlist = new HashMap<String, component>();
	static List<_task> tasklist = new ArrayList<_task>();

	static List<linkpoint> portlist = new ArrayList<linkpoint>();
	static List<_require> requirelist = new ArrayList<_require>();
	static List<_provide> providelist = new ArrayList<_provide>();

	static List<_exception> exceptionlist = new ArrayList<_exception>();
	static List<device> devicelist = new ArrayList<device>();
	static List<bus> buslist = new ArrayList<bus>();
	static List<rtos> rtoslist = new ArrayList<rtos>();

	static List<shareddataaccess> sdalist = new ArrayList<shareddataaccess>();
	static List<syncinterface> synclist = new ArrayList<syncinterface>();
	static List<ASyncMessaging> ASMlist = new ArrayList<ASyncMessaging>();

	static List<communicationchannel> cclist = new ArrayList<communicationchannel>();
	static List<InvocationChannel> ivclist = new ArrayList<InvocationChannel>();
	static List<DispatchChannel> dpclist = new ArrayList<DispatchChannel>();
	static List<MessageChannel> mclist = new ArrayList<MessageChannel>();

	static List<_event> eventlist = new ArrayList<_event>();
	static List<_state> statelist = new ArrayList<_state>();

	static List<transition> tslist = new ArrayList<transition>();
	static List<transitionstate> ts_slist = new ArrayList<transitionstate>();

	static List<_partition> partitionlist = new ArrayList<_partition>();
	static List<taskschedule> tscList = new ArrayList<taskschedule>();
	@Autowired
	private connectionsMapper cm;
	@Autowired
	private taskscheduleMapper tscmapper;
	@Autowired
	private componentMapper camArchMapper;
	@Autowired
	private linkpointMapper portsMapper;
	@Autowired
	private _provideMapper pM;
	@Autowired
	private _requireMapper rm;
	@Autowired
	private transitionMapper tm;
	@Autowired
	private transitionstateMapper tsm;
	@Autowired
	private _eventMapper em;
	@Autowired
	private _stateMapper sm;
	@Autowired
	private deviceMapper dMapper;
	@Autowired
	private busMapper bm;
	@Autowired
	private communicationchannelMapper cchannelMapper;
	@Autowired
	private MessageChannelMapper mcm;
	@Autowired
	private InvocationChannelMapper ivcm;
	@Autowired
	private _exceptionMapper _em;
	@Autowired
	private rtosMapper rsmMapper;
	@Autowired
	private _taskMapper _tm;
	@Autowired
	private _partitionMapper ptm;
	@Autowired
	private componenttransitionMapper cttm;
	@Autowired
	private dataobjectMapper dobjm;
	@Autowired
	private propagationMapper propgm;
	@Autowired
	private errorpathMapper epm;

	private int insert_partition(_partition p) {
		return ptm.insert(p);
	}

	private int insert_component(component c) {
		return camArchMapper.insert(c);
	}

	private int insert_bus(bus b) {
		return bm.insert(b);
	}

	private int insert_ports(linkpoint p) {
		return portsMapper.insert(p);
	}

	private int insert_require(_require r) {
		return rm.insert(r);
	}

	private int insert_provide(_provide p) {
		return pM.insert(p);
	}

	private int insert_cchannel(communicationchannel c) {
		return cchannelMapper.insert(c);
	}

	private int insert_mchannel(MessageChannel m) {
		return mcm.insert(m);
	}

	private int insert_ivcchannel(InvocationChannel i) {
		return ivcm.insert(i);
	}

	private int insert_device(device d) {
		return dMapper.insert(d);
	}

	private int insert_task(_task t) {
		return _tm.insert(t);
	}

	private int insert_transition(transition t) {
		return tm.insert(t);
	}

	private int insert_tss(transitionstate ts) {
		return tsm.insert(ts);
	}

	private int insert_rtos(rtos r) {
		return rsmMapper.insert(r);
	}

	private int insert_state(_state s) {
		return sm.insert(s);
	}

	private int insert_event(_event e) {
		return em.insert(e);
	}

	private int insert_exception(_exception e) {
		return _em.insert(e);
	}

	public Integer getPortIDByComponentName(String name, String portname) {
//		Integer aIntegers = camArchMapper.getPortIDByComponentName(name, portname);
		return camArchMapper.getPortIDByComponentName(name, portname);
	}

	public Integer getCmpIDbyName(String name) {
		return camArchMapper.getIDbyName(name).getComponentid();
	}

	public Integer getCChannelBysd(Integer sid, Integer did) {
		return cchannelMapper.getCChannelBysd(sid, did);
	}

	public Integer getEventID(String eventname) {
		return em.getEventID(eventname).getEventid();
	}

	public Integer getStateID(String statename) {
		return sm.getStateID(statename);
	}

	public void setAadlFiles(Map<String, String> aadlFiles) {
		this.aadlFiles = aadlFiles;
	}

	public static Document ModelResolver(String url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	// ????????????????????????????????????
	public void srvmatchmeta() throws Exception {
		AADLResolver.modeldirectory = "src/main/resources/modelresource/MarkedModelFile/";
		AADLResolver.compositelibfile = aadlFiles.get("?????????");
		AADLResolver.errlibfile = aadlFiles.get("?????????");
		AADLResolver.hardmodelfile = aadlFiles.get("????????????");

		ComponentResolver(hardmodelfile, "????????????");
		rtoslist.forEach((v) -> {
			insert_rtos(v);
		});
		buslist.forEach((v) -> {
			insert_bus(v);
		});
		devicelist.forEach((v) -> {
			insert_device(v);
		});
		String[] innersysfile = aadlFiles.get("??????????????????").split(";");
		for (String s : innersysfile) {
			if (s != null) {

				AADLResolver.dynamicfilename = s;
				InnerSystem(dynamicfilename);

//				Document d = ModelResolver(s);
//				List<? extends Node> nodes = d
//						.selectNodes("//ownedClassifier[@xsi:type='aadl2:SystemImplementation']");
//				for (Node n2 : nodes) {
//					StateResolver(s, ((Element) n2).attributeValue("id"));
//				}

				scheduleResolver(s);
				partitionResolver(s);
			}

		}
		MatchCChannel(hardmodelfile, "aadl");
		cclist.forEach((v) -> {
			insert_cchannel(v);
		});
		mclist.forEach((v) -> {
			insert_mchannel(v);
		});
		ivclist.forEach((v) -> {
			insert_ivcchannel(v);
		});
		exceptionlist.forEach((v) -> {
			insert_exception(v);
		});

	}

	/* ??????????????????????????????port???task */
	public void ComponentResolver(String filepath, String contenttype) throws Exception {
		Document document = ModelResolver(filepath);
		if (contenttype.equals("????????????")) {
			Element systemElement = (Element) document.selectSingleNode("//ownedClassifier[@name='isolette']");
			component system = new component();
			system.setName(systemElement.attributeValue("name"));
			system.setModeltype("aadl");
			system.setType("uniquesystem");
			// ???????????????????????????
			system.setWcet(systemElement.element("ownedPropertyAssociation").element("ownedValue").element("ownedValue")
					.attributeValue("value") + "ms");
			Integer id = (int) GetID.getId();
			system.setComponentid(id);
			insert_component(system);
		}
		// ???hardware?????????????????????????????????????????????
		String getbus = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedBusSubcomponent";
		String getsys = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedSystemSubcomponent";
		String getdevice = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedDeviceSubcomponent";
		components = document.selectNodes(getbus);
		ResolveComponents(filepath, "bus");
		components = document.selectNodes(getsys);
		ResolveComponents(filepath, "sys");
		components = document.selectNodes(getdevice);
		ResolveComponents(filepath, "device");
	}

	// filename?????????hardwareArchitecture
	public void MatchCChannel(String modelfilename, String modelType) throws Exception {
		Document document = ModelResolver(modelfilename);
		List<String> namelist = new ArrayList<>();
		if (modelType.equals("aadl")) {
			String getCChannel = "";
			// hardware??????cchannel??????
			// bus access?????????asyncmessaging
			String getmessagechannel = "//ownedPublicSection/ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedAccessConnection";
			cchannels = document.selectNodes(getmessagechannel);
			ResolveCChannel(modelfilename, "asyncmessaging");
			// invocationChannel
			// aadl??????????????????????????????
			String getsync = "//ownedPublicSection/ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedPortConnection";
			cchannels = document.selectNodes(getsync);
			ResolveCChannel(modelfilename, "sync");
			// dispatchChannel
			// TODO ??????????????????dispatchChannel?????????xpath
//			String getdc = "//ownedPublicSection/";
//			cchannels = document.selectNodes(getdc);
//			ResolveCChannel(modelfilename, "dispatchChannel"); 
		}
	}

	private void ResolveCChannel(String modelfilename, String t) throws Exception {
		for (Node n : cchannels) {
			Element element = (Element) n;
			communicationchannel cchannel = new communicationchannel();
			Integer idString = (int) GetID.getId();
			cchannel.setName(element.attributeValue("name"));
			cchannel.setCommunicationchannelid(idString);
			cchannel.setModeltype("aadl");
			switch (t) {
			case "asyncmessaging":
				// TODO ??????dispatch???source,dest
				// TODO ??????inbuffer,outbuffer
				// context???????????????????????????????????????
				// connectionEnd?????????????????????
				// source???context????????????bus???????????????context???????????????bus?????????
				if (element.element("source").attribute("context") != null) {
					// inbuffer
					// ??????source
					// ?????????????????????????????????????????????????????????????????????????????????id
					String CompositeName = GetName(modelfilename, element.element("source").attributeValue("context"));
					String PortName = GetName(compositelibfile,
							element.element("source").attributeValue("connectionEnd"));

					Integer sourceportid = getPortIDByComponentName(CompositeName, PortName);
					cchannel.setSourceid(sourceportid);
				} else {
					// outbuffer
					// ??????source???bus
					String busname = GetName(modelfilename, element.element("source").attributeValue("connectionEnd"));
					Integer busid = getCmpIDbyName(busname);
					cchannel.setSourceid(busid);
				}
				// dest??????context????????????bus????????????context???????????????bus?????????
				if (element.element("destination").attribute("context") == null) {
					// inbuffer
					// dest???bus
					String busname = GetName(modelfilename,
							element.element("destination").attributeValue("connectionEnd"));
					Integer busid = getCmpIDbyName(busname);
					cchannel.setDestid(busid);
				} else {
					// outbuffer
					// dest?????????
					String CompositeName = GetName(modelfilename,
							element.element("destination").attributeValue("context"));
					String PortName = GetName(compositelibfile,
							element.element("destination").attributeValue("connectionEnd"));

					Integer destportid = getPortIDByComponentName(CompositeName, PortName);
					cchannel.setDestid(destportid);
				}
				cchannel.setType("asyncmessaging");
				MessageChannel b = new MessageChannel();
				b.setMessagechannelid(idString);
				mclist.add(b);
				break;
			case "sync":
//				String CompositeName = GetName(modelfilename, element.element("source").attributeValue("context"));
//				String PortName = GetName(compositelibfile, element.element("source").attributeValue("connectionEnd"));
//				Integer sourceportid = getPortIDByComponentName(CompositeName, PortName);
				// ?????????????????????
				String CompositeFIleName = modeldirectory
						+ Getfilename(element.element("source").attributeValue("connectionEnd"));
				String sourceportid = GetElementID(CompositeFIleName,
						element.element("source").attributeValue("connectionEnd"));
				if (sourceportid.equals("")) {
					cchannel.setSourceid(0);
				} else {
					cchannel.setSourceid(Integer.valueOf(sourceportid));
				}

//				String CompositeName1 = GetName(modelfilename,
//						element.element("destination").attributeValue("context"));
				String CompositeName1 = modeldirectory
						+ Getfilename(element.element("destination").attributeValue("connectionEnd"));
//				String PortName1 = GetName(compositelibfile,
//						element.element("destination").attributeValue("connectionEnd"));
				String destid = GetElementID(CompositeName1,
						element.element("destination").attributeValue("connectionEnd"));
				// Integer destportid = getPortIDByComponentName(CompositeName1, PortName1);
				// ?????????source dest
				if (destid.equals("")) {
					cchannel.setDestid(0);
				} else {
					cchannel.setDestid(Integer.valueOf(destid));
				}
				cchannel.setType("sync");
				InvocationChannel r = new InvocationChannel();
				r.setInvocationchannelid(idString);
				ivclist.add(r);
				break;
			case "dispatchChannel":
				cchannel.setType("dispatchChannel");
				DispatchChannel d = new DispatchChannel();
				d.setDispatchchannelid(idString);
				dpclist.add(d);
				break;
			}
			cclist.add(cchannel);
		}
	}

	public void initComponentID(String filepath) throws Exception {
		String getbus = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedBusSubcomponent";
		String getdevice = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedDeviceSubcomponent";
		String getsys = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedSystemSubcomponent";
		Document document = ModelResolver(filepath);
		List<? extends Node> nodes = document.selectNodes(getbus);
		for (Node n : nodes) {
			Integer idString = (int) GetID.getId();
			AppendID.AppendID(filepath, n.getUniquePath(), idString.toString());
		}
		nodes = document.selectNodes(getsys);
		nodes.forEach((v) -> {
			Integer idString2 = (int) GetID.getId();
			try {
				AppendID.AppendID(filepath, v.getUniquePath(), idString2.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		nodes = document.selectNodes(getdevice);
		for (Node n : nodes) {
			Integer idString3 = (int) GetID.getId();
			AppendID.AppendID(filepath, n.getUniquePath(), idString3.toString());
		}
	}

	// TODO rtos???processor???????????????
	public void InnerSystem(String modelfilename) throws Exception {
		Document document = ModelResolver(modelfilename);
		String getsys = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']";
		Element sysElement = (Element) document.selectSingleNode(getsys);

		// ???????????????????????????????????????task??????????????????shcedule???????????????
		// String gettask = "//ownedClassifier[@xsi:type='aadl2:ProcessType' or
		// @xsi:type='aadl2:ThreadType']";
		String gettask = "//ownedClassifier[@xsi:type='aadl2:ProcessType']";
		// ???????????????????????????

		String innerDevcvice = "//ownedClassifier[@xsi:type='aadl2:ProcessorType']";
		components = document.selectNodes(innerDevcvice);
		resolverChild(modelfilename, "device");

		components = document.selectNodes(gettask);
		TaskResolver(modelfilename, Integer.valueOf(sysElement.attributeValue("id")));
		// task????????????
		document = ModelResolver(modelfilename);
		List<Node> portconnectionlist = document
				.selectNodes("//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedPortConnection");
		for (Node n2 : portconnectionlist) {
			Element e = (Element) n2;
			connections c = new connections();
			c.setFathercmpid(e.getParent().attributeValue("id"));
			if (e.element("source").attributeValue("context") != null) {
				// ??????context??????????????????
				Element declare = (Element) document
						.selectSingleNode(GetXPath(e.element("source").attributeValue("context")));
				// ?????????????????????
				Element sourcecomponent = (Element) document
						.selectSingleNode(GetXPath42layer(declare.attributeValue("processSubcomponentType")));
				c.setStartcomponentid(Integer.valueOf(sourcecomponent.attributeValue("id")));
			} else {
				c.setStartcomponentid(Integer.valueOf(e.getParent().attributeValue("id")));
			}
			Element sourceport = (Element) document
					.selectSingleNode(GetXPath(e.element("source").attributeValue("connectionEnd")));
			c.setStartinterface(sourceport.attributeValue("id"));
			if (e.element("destination").attributeValue("context") != null) {
				Element declare = (Element) document
						.selectSingleNode(GetXPath(e.element("destination").attributeValue("context")));
				Element dstcomponent = (Element) document
						.selectSingleNode(GetXPath42layer(declare.attributeValue("processSubcomponentType")));
				c.setEndcomponentid(Integer.valueOf(dstcomponent.attributeValue("id")));

			} else {
				c.setEndcomponentid(Integer.valueOf(e.getParent().attributeValue("id")));
			}
			Element dstport = (Element) document
					.selectSingleNode(GetXPath(e.element("destination").attributeValue("connectionEnd")));
			c.setEndinterface(dstport.attributeValue("id"));
			cm.insert(c);
		}
		// SystemType???????????????????????????????????????

		String getallsysimpl = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']";
		List<? extends Node> nodes = document.selectNodes(getallsysimpl);
		for (Node n : nodes) {
			Element e = (Element) n;
			// StateResolver(modelfilename, e.attributeValue("id"));
		}
	}

	// ?????????deivce??????????????????????????????component??????
	private void resolverChild(String filepath, String t) throws Exception {
		List<? extends Node> ports = null;
		for (Node n : components) {
			Element element = (Element) n;
			Integer idString2 = (int) GetID.getId();

			AppendID.AppendID(filepath, n.getUniquePath(), idString2.toString());

			component component = new component();
			component.setModeltype("aadl");
			component.setName(element.attributeValue("name"));
			component.setComponentid(idString2);
			component.setType("partition");

			insert_component(component);
			// partition
//			String a=element.attributeValue("xsi:type");
//			if (element.attributeValue("xsi:type").equals("aadl2:ProcessorType")) {
			// _partition partition = new _partition();
			// partition.setPartitionid(idString2);
			// partition.setRtosid();
			// partitionlist.add(partition);
			// }
			// RTOS???partition???????????????
//			device d = new device();
//			d.setDeviceid(idString2);
//			devicelist.add(d);
			// ?????????linkpoint
			String devicedef = modeldirectory + Getfilename(element.attributeValue("deviceSubcomponentType"));
			LinkpointResolver(devicedef, ports, idString2, t, element.attributeValue("name"));

			// componentlist.put(element.attributeValue("name"), component);
		}
	}

	private void ResolveComponents(String filepath, String t) throws Exception {
		List<? extends Node> ports = null;
		for (Node n : components) {
			Element element = (Element) n;
			component component = new component();

			Integer componentID = Integer.valueOf(element.attributeValue("id"));

			component.setModeltype("aadl");
			component.setName(element.attributeValue("name"));
			component.setComponentid(componentID);

			switch (t) {
			case "bus":
				component.setType("bus");
				camArchMapper.insert(component);
				bus b = new bus();
				b.setBusid(componentID);
				buslist.add(b);
				break;
			case "sys":
				dynamicfilename = Getfilename(element.attributeValue("systemSubcomponentType"));
//				String getwcet = "//ownedClassifier[@xsi:type='aadl2:SystemType' and @name='" + component.getName()
//				+ "']//ownedPropertyAssociation[contains(@property,'wcet4sys')]/ownedValue/ownedValue";
//		
//		Element wcetElement = (Element) document.selectSingleNode(getwcet);
//		component.setWcet(wcetElement.attributeValue("value") + "ms");
				component.setType("rtos");

				String getwcetinsys = "//ownedPublicSection/ownedClassifier[@name='" + component.getName()
						+ "']/ownedPropertyAssociation[contains(@property,'wcet4sys')]";
				Document document1 = ModelResolver(modeldirectory + dynamicfilename);
				Element sysElement = (Element) document1.selectSingleNode(getwcetinsys);
				if (sysElement != null) {
					component.setWcet(
							sysElement.element("ownedValue").element("ownedValue").attributeValue("value") + "ms");
				}

				camArchMapper.insert(component);
				rtos r = new rtos();
				r.setRtosid(componentID);
				// TODO ??????partitions?????????
				rtoslist.add(r);

				String sysdef = modeldirectory + Getfilename(element.attributeValue("systemSubcomponentType"));
				LinkpointResolver(sysdef, ports, componentID, t, element.attributeValue("name"));
				// ??????state?????????????????????transition

				ExceptionResolver(dynamicfilename, componentID.toString());
				break;
			case "device":
				component.setType("device");
				String getwcet = "//ownedPublicSection/ownedClassifier[@name='" + component.getName()
						+ "']/ownedPropertyAssociation[contains(@property,'wcet4dev')]";
				Document document = ModelResolver(compositelibfile);
				Element devicElement = (Element) document.selectSingleNode(getwcet);
				if (devicElement != null) {
					component.setWcet(
							devicElement.element("ownedValue").element("ownedValue").attributeValue("value") + "ms");
				}
				camArchMapper.insert(component);
				device d = new device();
				d.setDeviceid(componentID);
				devicelist.add(d);
				// ?????????linkpoint
				// String devicedef = modeldirectory +
				// Getfilename(element.attributeValue("deviceSubcomponentType"));

				LinkpointResolver(compositelibfile, ports, componentID, t, element.attributeValue("name"));
				break;
			}
		}
	}

	private void LinkpointResolver(String linkpointfile, List<? extends Node> ports, Integer fatherid,
			String fathertype, String componetname) throws Exception {
		switch (fathertype) {
		case "bus":
		case "device":
			Document document = ModelResolver(compositelibfile);
			// ??????linkpoint,????????????busaccess???dataport,eventport,busaccess???require??????,dataport???????????????
			// bussaccess
			ports = document
					.selectNodes("//ownedPublicSection/ownedClassifier[@name='" + componetname + "']/ownedBusAccess");
			TraverseOwnedPorts(ports, fatherid, "busaccess");
			// dataport
			ports = document
					.selectNodes("//ownedPublicSection/ownedClassifier[@name='" + componetname + "']/ownedDataPort");
			TraverseOwnedPorts(ports, fatherid, "dataport");
			// eventport
			ports = document
					.selectNodes("//ownedPublicSection/ownedClassifier[@name='" + componetname + "']/ownedEventPort");
			// ???????????????busaccess??????----- eventport??????------ dataport??????--------
			TraverseOwnedPorts(ports, fatherid, "eventport");
			break;
		case "sys":
			Document document2 = ModelResolver(linkpointfile);
			dynamicfilename = linkpointfile;
			// ??????
			ports = document2.selectNodes("//ownedClassifier[@xsi:type='aadl2:SystemType']/ownedDataPort");
			TraverseOwnedPorts4Sys(ports, fatherid, "dataport");
			ports = document2.selectNodes("//ownedClassifier[@xsi:type='aadl2:SystemType']/ownedEventPort");
			TraverseOwnedPorts4Sys(ports, fatherid, "eventport");
			ports = document2.selectNodes("//ownedClassifier[@xsi:type='aadl2:SystemType']/ownedBusAccess");
			TraverseOwnedPorts4Sys(ports, fatherid, "busaccess");
			break;
		}
	}

	private void TraverseOwnedPorts4Sys(List<? extends Node> ports, Integer fatherid, String portType)
			throws Exception {
		for (Node n2 : ports) {
			Element portElement = (Element) n2;

			linkpoint ports1 = new linkpoint();
			// ???????????????name?????????
			ports1.setName(portElement.attributeValue("name"));
			ports1.setModeltype("aadl");
			Integer linkpointID = (int) GetID.getId();
			ports1.setLinkpointid(linkpointID);
			// ??????data
			// ?????????????????????????????????composition??????data
			try {
				String[] datapath = portElement.attributeValue("dataFeatureClassifier").split("\\.");
				String f = Getfilename(portElement.attributeValue("dataFeatureClassifier"));
				Document d = ModelResolver(modeldirectory + f);
				List<Node> datalist = d
						.selectNodes("//ownedClassifier[@xsi:type='aadl2:DataType' and @name='" + datapath[2] + "']");
				for (Node node : datalist) {
					Element dataElement = (Element) node;
					List<Element> props = dataElement.elements("ownedPropertyAssociation");
					for (Element e : props) {
						if (e.attributeValue("property").contains("period")) {
							ports1.setPeriod(
									e.element("ownedValue").element("ownedValue").attributeValue("value") + "ms");
						}
						if(e.attributeValue("property").contains("protocol")) {
							ports1.setProtocol(e.element("ownedValue").element("ownedValue").attributeValue("value"));
						}
						if (e.attributeValue("property").contains("porttype")) {

							dataobject doj = new dataobject();
							doj.setDatatype(e.element("ownedValue").element("ownedValue").attributeValue("value"));
							doj.setFrom(linkpointID);
							dobjm.insert(doj);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			portsMapper.insert(ports1);

			switch (portType) {
			case "dataport":
			case "eventport":
				syncinterface si = new syncinterface();
				si.setSyncinterfaceid(linkpointID);
				try {
					AppendID.AppendID(dynamicfilename, portElement.getUniquePath(), linkpointID.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (!(portElement.attribute("in") == null)) {
					// ???????????????
					_require r = new _require();
					r.setRequired(linkpointID);
					r.setRequirer(fatherid);
					rm.insert(r);
					requirelist.add(r);
				} else {
					_provide p = new _provide();
					p.setProvided(linkpointID);
					p.setProvider(fatherid);
					pM.insert(p);
					providelist.add(p);
				}
				break;
			case "busaccess":
				AppendID.AppendID(dynamicfilename, portElement.getUniquePath(), linkpointID.toString());
				ASyncMessaging asm = new ASyncMessaging();
				asm.setAsyncmessagingid(linkpointID);
				if (portElement.attribute("kind").equals("requires")) {
					// ???????????????
					_require r = new _require();
					r.setRequired(linkpointID);
					r.setRequirer(fatherid);
					rm.insert(r);
					requirelist.add(r);
				} else {
					_provide p = new _provide();
					p.setProvided(linkpointID);
					p.setProvider(fatherid);
					pM.insert(p);
					providelist.add(p);
				}
				break;
			}

			portlist.add(ports1);
		}
	}

	private void TraverseOwnedPorts(List<? extends Node> ports, Integer fatherid, String portType) throws Exception {
		for (Node n2 : ports) {
			Element element2 = (Element) n2;

			linkpoint ports1 = new linkpoint();
			// ???????????????name?????????
			ports1.setName(element2.attributeValue("name"));
			ports1.setModeltype("aadl");
			Integer linkpointID = (int) GetID.getId();
			ports1.setLinkpointid(linkpointID);
			portsMapper.insert(ports1);
			switch (portType) {
			case "dataport":
			case "eventport":
				syncinterface si = new syncinterface();
				si.setSyncinterfaceid(linkpointID);
				try {

					AppendID.AppendID(compositelibfile, element2.getUniquePath(), linkpointID.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (!(element2.attribute("in") == null)) {
					// ???????????????
					_require r = new _require();
					r.setRequired(linkpointID);
					r.setRequirer(fatherid);
					rm.insert(r);
				} else {
					_provide p = new _provide();
					p.setProvided(linkpointID);
					p.setProvider(fatherid);
					pM.insert(p);
				}
				break;
			case "busaccess":
				AppendID.AppendID(compositelibfile, element2.getUniquePath(), linkpointID.toString());
				ASyncMessaging asm = new ASyncMessaging();
				asm.setAsyncmessagingid(linkpointID);
				if (element2.attribute("kind").equals("requires")) {
					// ???????????????
					_require r = new _require();
					r.setRequired(linkpointID);
					r.setRequirer(fatherid);
					rm.insert(r);
				} else {
					_provide p = new _provide();
					p.setProvided(linkpointID);
					p.setProvider(fatherid);
					pM.insert(p);
				}
				break;
			}
		}
	}

	private void TraverseOwnedPorts4task(String modelfile, List<? extends Node> ports, Integer fatherid,
			String portType) throws Exception {
		for (Node n2 : ports) {
			Element taskportElement = (Element) n2;

			linkpoint ports1 = new linkpoint();
			// ???????????????name?????????
			ports1.setName(taskportElement.attributeValue("name"));
			ports1.setModeltype("aadl");
			Integer linkpointID = (int) GetID.getId();
			ports1.setLinkpointid(linkpointID);
			portsMapper.insert(ports1);
			switch (portType) {
			case "dataport":
			case "eventport":
				syncinterface si = new syncinterface();
				si.setSyncinterfaceid(linkpointID);
				try {

					AppendID.AppendID(modelfile, taskportElement.getUniquePath(), linkpointID.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (!(taskportElement.attribute("in") == null)) {
					// ???????????????
					_require r = new _require();
					r.setRequired(linkpointID);
					r.setRequirer(fatherid);
					rm.insert(r);
				} else {
					_provide p = new _provide();
					p.setProvided(linkpointID);
					p.setProvider(fatherid);
					pM.insert(p);
				}
				break;
			case "busaccess":
				AppendID.AppendID(compositelibfile, taskportElement.getUniquePath(), linkpointID.toString());
				ASyncMessaging asm = new ASyncMessaging();
				asm.setAsyncmessagingid(linkpointID);
				if (taskportElement.attribute("kind").equals("requires")) {
					// ???????????????
					_require r = new _require();
					r.setRequired(linkpointID);
					r.setRequirer(fatherid);
					rm.insert(r);
				} else {
					_provide p = new _provide();
					p.setProvided(linkpointID);
					p.setProvider(fatherid);
					pM.insert(p);
				}
				break;
			}
		}
	}

	public void ExceptionResolver4Task(String modelfilename, String fatherpath, Integer taskid) throws Exception {
		Document document = ModelResolver(modelfilename);
		// propagation?????????
		List<Node> nodelist = document
				.selectNodes(fatherpath + "/ownedAnnexSubclause/parsedAnnexSubclause/propagations");
		nodelist.forEach((v) -> {
			Element e = (Element) v;
			Element portElement = e.element("featureorPPRef");
			_exception taskException = new _exception();
			Integer exceptionid = (int) GetID.getId();
			taskException.setComponentid(taskid);

			List<Element> namelist = e.element("typeSet").elements("typeTokens");
			StringBuffer sbname = new StringBuffer();
			namelist.forEach((namev) -> {
				sbname.append(getType(namev.attributeValue("type")) + "???");
			});
			taskException.setType(sbname.toString());

			taskException.setName("task?????????");
			try {
				taskException.setLinkpointid(Integer.valueOf(
						GetElementID(modelfilename, e.element("featureorPPRef").attributeValue("featureorPP"))));
			} catch (Exception err) {
			}

			insert_exception(taskException);
		});

	}

	// ?????????????????????
	public void ExceptionResolver(String modelfilename, String componentID) throws Exception {
		Document document = ModelResolver(modelfilename);
		List<? extends Node> nodelist = new ArrayList<>();

		// ??????system??????
		nodelist = document.selectNodes("//ownedClassifier[@xsi:type='aadl2:SystemType']");
		nodelist.forEach((v) -> {
			Element systemElement = (Element) v;

			try {
				// ??????????????????????????????behavior?????????????????????????????????????????????
				if (systemElement.element("ownedAnnexSubclause") != null) {
					if (systemElement.element("ownedAnnexSubclause").element("parsedAnnexSubclause")
							.attribute("useBehavior") != null) {

						String[] behaviorcontent = systemElement.element("ownedAnnexSubclause")
								.element("parsedAnnexSubclause").attributeValue("useBehavior").split("\\.");
						String behaviorname = behaviorcontent[2];
						// TODO ????????????state?????????
						StateResolver(modelfilename, componentID);
						// ??????event
						eventResolver(modelfilename, "aadl");
						// ??????transition
						TransitionResolver(behaviorname, componentID);
					}

					// ??????propagation,useType????????????????????????????????????????????????p

					List<Element> propagationList = systemElement.element("ownedAnnexSubclause")
							.element("parsedAnnexSubclause").elements("propagations");
					propagationList.forEach((pv) -> {

						// out propagation
						List<Element> faultList = pv.element("typeSet").elements("typeTokens");
						faultList.forEach((fv) -> {
							String getportPath = pv.element("featureorPPRef").attributeValue("featureorPP");
							Element portElement = (Element) document.selectSingleNode(GetXPath(getportPath));

							// try {
							propagation p = new propagation();
							p.setFaultname(fv.attributeValue("type").split("\\.")[2]);
							p.setComponentid(componentID);
							p.setLinkpointid(portElement.attributeValue("id"));
							propgm.insert(p);
//							} catch (Exception e1) {
//								e1.printStackTrace();
//							}
						});

					});
					// ??????error flow
					List<Element> flowList = systemElement.element("ownedAnnexSubclause")
							.element("parsedAnnexSubclause").elements("flows");
					flowList.forEach((fv) -> {
						errorpath ep = new errorpath();
						ep.setComponentid(componentID);

						String sourcepropagationpath = GetXPath4State(fv.attributeValue("incoming"));
						Element spropElement = (Element) document
								.selectSingleNode(sourcepropagationpath);
						Element portElement = (Element) document.selectSingleNode(
								GetXPath(spropElement.element("featureorPPRef").attributeValue("featureorPP")));
						String sourceportid = portElement.attributeValue("id");
						ep.setStartportid(sourceportid);
						String destpropagationpath = GetXPath4State(fv.attributeValue("outgoing"));
						spropElement = (Element) document.selectSingleNode(destpropagationpath);
						portElement = (Element) document.selectSingleNode(
								GetXPath(spropElement.element("featureorPPRef").attributeValue("featureorPP")));
						String destportid = portElement.attributeValue("id");
						ep.setEndportid(destportid);

						epm.insert(ep);
					});
				}
			} catch (Exception e1) {

				e1.printStackTrace();
			}
		});
	}

	// ????????????????????????????????????????????????????????????????????????event
	public void TransitionResolver(String behaviorname, String cmpid) throws Exception {
		Document document = ModelResolver(errlibfile);
		List<? extends Node> transNodes = document
				.selectNodes("//parsedAnnexLibrary/behaviors[@name='" + behaviorname + "']/transitions");

		transNodes.forEach((v) -> {
			Element transitionElement = (Element) v;
			// transition?????????trigger???event??????
			String getTriggerevent = transitionElement.element("condition")
					.element("qualifiedErrorPropagationReference").element("emv2Target").attributeValue("namedElement");

			transition t = new transition();
			try {
				Integer transitionid = (int) GetID.getId();
				AppendID.AppendID(errlibfile, v.getUniquePath(), transitionid.toString());

				t.setTransitionid(transitionid);
				// ??????event
				Document d = ModelResolver(errlibfile);
				String eventpathString = GetXPath4State(getTriggerevent);
				Node nodes = d.selectSingleNode(eventpathString);
				String id = ((Element) nodes).attributeValue("id");
				t.setTriggerid(Integer.valueOf(id));
				t.setName(getType(transitionElement.attributeValue("name")));

				insert_transition(t);
				transitionstate tsTransitionstate = new transitionstate();

				// ????????????????????????state
				Element stateelement = (Element) d
						.selectSingleNode(GetXPath4State(transitionElement.attributeValue("source")));
				//System.out.println(stateelement.attributeValue("id"));
				tsTransitionstate.setSourceid(Integer.valueOf(stateelement.attributeValue("id")));
				if (transitionElement.attributeValue("target") != null) {
					stateelement = (Element) document
							.selectSingleNode(GetXPath4State(transitionElement.attributeValue("target")));
					tsTransitionstate.setOutid(Integer.valueOf(stateelement.attributeValue("id")));

					tsTransitionstate.setTransitionid(t.getTransitionid());
					insert_tss(tsTransitionstate);

					componenttransition cmpts = new componenttransition();
					cmpts.setComponentid(cmpid);
					cmpts.setTransitionid(t.getTransitionid().toString());
					cttm.insert(cmpts);
				}
			} catch (Exception e) {
				//System.out.println("??????????????????" + transitionElement.getName() + "?????????" + behaviorname);
				//System.out.println(cmpid);
				e.printStackTrace();
			}
		});
	}

	// ??????????????????
	public void eventResolver(String modelfilename, String modelType) throws Exception {
		// TODO ?????????event?????????
		// ?????????event
		Document d = ModelResolver(errlibfile);
		List<? extends Node> outevent = d
				.selectNodes("//ownedPublicSection/ownedAnnexLibrary/parsedAnnexLibrary/behaviors/events");
		for (Node n : outevent) {
			Element element = (Element) n;
			_event e2 = new _event();
			e2.setName(element.attributeValue("name"));
			Integer idString = (int) GetID.getId();
			e2.setEventid(idString);
			AppendID.AppendID(errlibfile, n.getUniquePath(), idString.toString());
			insert_event(e2);
//			eventlist.add(e2);
		}
		// ??????????????????event
		Document document = ModelResolver(modelfilename);
		List<? extends Node> erroreventName = document.selectNodes("//ownedAnnexSubclause/parsedAnnexSubclause/events");
		erroreventName.forEach((v) -> {
			Element element = (Element) v;
			_event e2 = new _event();
			e2.setName(element.attributeValue("name"));
			Integer idString = (int) GetID.getId();
			try {
				AppendID.AppendID(modelfilename, v.getUniquePath(), idString.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			insert_event(e2);
//			eventlist.add(e2);
		});

	}

	// ??????????????????????????????,state????????????state
	private void StateResolver(String modelfilename, String Compositeid) throws Exception {
		Document document = ModelResolver(modelfilename);

		List<? extends Node> stateInfo = document
				.selectNodes("//ownedAnnexSubclause/parsedAnnexSubclause/states/condition/operands/operands");
		if (stateInfo.size() != 0) {
			// ??????????????????state
			stateInfo.forEach((v) -> {
				Element element = (Element) v;
				try {
					AppendID.AppendID(modelfilename, v.getUniquePath(), Compositeid);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				String statenameString = element.element("qualifiedState").attributeValue("state");

				String cmpID = element.element("qualifiedState").element("subcomponent").attributeValue("subcomponent");
				_state s = new _state();
				try {
					// s.setComponentid(getCmpIDbyName(GetName(hardmodelfile, GetXPath(cmpID))));
					s.setComponentid(Integer.valueOf(Compositeid));
					s.setName(GetName(errlibfile, GetXPath4State(statenameString)));
				} catch (Exception e) {
				}
				insert_state(s);
//				statelist.add(s);
			});
		}
		// 5.10????????????????????????????????????behavior??????????????????behavior??????state??????????????????
		Node behaviorInfo = document.selectSingleNode("//ownedAnnexSubclause/parsedAnnexSubclause");
		if (behaviorInfo != null) {
			Element e1 = (Element) behaviorInfo;
			try {
				String s = "//ownedAnnexLibrary/parsedAnnexLibrary/behaviors[@name='"
						+ getType(e1.attributeValue("useBehavior")) + "']/states";
				List<? extends Node> libstateinfo = ModelResolver(errlibfile).selectNodes(s);
				libstateinfo.forEach((v) -> {
					Integer idString = (int) GetID.getId();
					try {
						AppendID.AppendID(errlibfile, v.getUniquePath(), idString.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					_state stat = new _state();
					stat.setStateid(idString);
					stat.setComponentid(Integer.valueOf(Compositeid));
					stat.setName(((Element) v).attributeValue("name"));
					insert_state(stat);
//					statelist.add(stat);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ??????Exception?????????
	private static String getType(String typepath) {
		if (typepath != null && typepath.contains(".")) {

			String[] s = typepath.split("\\.");
			return s[s.length - 1];
		}
		return "";
	}

	private void TaskResolver(String modelfilename, Integer fatherid) throws Exception {

		for (Node n : components) {
			// ??????component
			Element taskElement = (Element) n;

			component component = new component();
			Integer idString = (int) GetID.getId();

			AppendID.AppendID(modelfilename, n.getUniquePath(), idString.toString());

			component.setComponentid(idString);

			component.setModeltype("aadl");
			component.setName(taskElement.attributeValue("name"));
			component.setType("task");
			insert_component(component);

			// ??????task
			_task t = new _task();
			t.setName(component.getName());
			t.setTaskid(idString);
			t.setFatherid(fatherid);
			List<Element> ports = taskElement.elements("ownedDataPort");
			TraverseOwnedPorts4task(modelfilename, ports, idString, "dataport");

			List<? extends Node> prop = taskElement.elements("ownedPropertyAssociation");
			for (Node n2 : prop) {
				Element e2 = (Element) n2;
				if (e2.attributeValue("property").contains("Deadline")) {
					String getdeadline = e2.element("ownedValue").element("ownedValue").attributeValue("value");
					t.setDeadline(getdeadline + "ms");
				}
				if (e2.attributeValue("property").contains("Period")) {
					String getperiod = e2.element("ownedValue").element("ownedValue").attributeValue("value");
					t.setPeriod(getperiod + "ms");
				}
				// ???process,??????????????????process???wcet??????wcet
				if (e2.attributeValue("property").contains("wcet")) {
					String getwcet = e2.element("ownedValue").element("ownedValue").attributeValue("value");
					t.setWcet(getwcet + "ms");
				}
			}
			// ??????impl?????????
			Document document = ModelResolver(modelfilename);
			String getimpl = taskElement.getUniquePath() + "/following-sibling::ownedClassifier[@name='"
					+ taskElement.attributeValue("name") + ".impl" + "']";

			threadResolver(taskElement, idString, getimpl, modelfilename);
			insert_task(t);

			// thread?????????
			// ???????????????,document???????????????
			document = ModelResolver(modelfilename);
			List<Node> portconnectionlist = document.selectNodes(getimpl + "/ownedPortConnection");
			for (Node n2 : portconnectionlist) {
				Element e = (Element) n2;
				connections c = new connections();
				c.setFathercmpid(e.getParent().attributeValue("id"));
				c.setConnectiontype(e.attributeValue("name"));
				if (e.element("source").attributeValue("context") != null) {
					// ??????context??????????????????
					Element sourcecomponent = (Element) document
							.selectSingleNode(GetXPath(e.element("source").attributeValue("context")));
					c.setStartcomponentid(Integer.valueOf(sourcecomponent.attributeValue("id")));
				} else {
					c.setStartcomponentid(Integer.valueOf(idString));
				}
				Element sourceport = (Element) document
						.selectSingleNode(GetXPath(e.element("source").attributeValue("connectionEnd")));
				c.setStartinterface(sourceport.attributeValue("id"));

				if (e.element("destination").attributeValue("context") != null) {
					Element dstcomponent = (Element) document
							.selectSingleNode(GetXPath(e.element("destination").attributeValue("context")));
					c.setEndcomponentid(Integer.valueOf(dstcomponent.attributeValue("id")));

				} else {
					c.setEndcomponentid(Integer.valueOf(idString));
				}
				Element dstport = (Element) document
						.selectSingleNode(GetXPath(e.element("destination").attributeValue("connectionEnd")));
				c.setEndinterface(dstport.attributeValue("id"));
				cm.insert(c);
			}
			ExceptionResolver4Task(modelfilename, n.getUniquePath(), idString);
		}
	}

	private void threadResolver(Element fatherdeclare, Integer fatherid, String rawimplpath, String modelfilename)
			throws Exception {
		Document d = ModelResolver(modelfilename);
		Element e = (Element) d.selectSingleNode(rawimplpath);
		AppendID.AppendID(modelfilename, e.getUniquePath(), fatherid.toString());
		// ?????????impl
		List<Element> thread = e.elements("ownedThreadSubcomponent");
		if (thread.size() > 0) {
			for (Element e2 : thread) {
				Element theadElement = (Element) d
						.selectSingleNode(GetXPath42layer(e2.attributeValue("threadSubcomponentType")));

				// ?????????impl???dataport?????????????????????????????????
				component component = new component();
				Integer idString = (int) GetID.getId();
				AppendID.AppendID(modelfilename, e2.getUniquePath(), idString.toString());
				AppendID.AppendID(modelfilename, theadElement.getUniquePath(), idString.toString());

				component.setComponentid(idString);
				component.setModeltype("aadl");
				component.setName(theadElement.attributeValue("name"));
				component.setType("task");
				insert_component(component);
				// ??????????????????impl....
				String getdeclare = theadElement.getUniquePath() + "/preceding-sibling::ownedClassifier[@name='"
						+ theadElement.attributeValue("name").split("\\.")[0] + "']";
				Element declareElement = (Element) d.selectSingleNode(getdeclare);

				List<Element> ports = declareElement.elements("ownedDataPort");
				TraverseOwnedPorts4task(modelfilename, ports, idString, "dataport");

				_task t = new _task();
				t.setName(theadElement.attributeValue("name"));

				t.setTaskid(idString);
				t.setFatherid(fatherid);
				insert_task(t);
			}
		}
	}

	private static String Getfilename(String systemSubcomponentType) {
		if (systemSubcomponentType != null) {
			String reg = "(?<=\\s)[A-Za-z0-9]+";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(systemSubcomponentType);
			ArrayList<String> result = new ArrayList<String>();
			while (matcher.find()) {
				result.add(matcher.group());
			}
			if (result.size() > 0) {
				return result.get(0) + ".aaxl2";
			}
		}
		return "";
	}

	// ??????partition????????????????????????
	private void partitionResolver(String filepath) throws Exception {
		Document document = ModelResolver(filepath);
		String getpartitionString = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedPropertyAssociation[contains(@property,'Binding')]";
		List<? extends Node> bindings = document.selectNodes(getpartitionString);

		for (Node n : bindings) {
			Element e = (Element) n;
			Element p1 = e.element("ownedValue").element("ownedValue").element("ownedListElement").element("path");
			// ??????binding????????????processor???????????????????????????partition??????
			// ???????????????????????????task???????????????task????????????partition????????????processor??????
			if (p1 != null) {
				// p1 = p1.element("path");
				String taskpath = e.element("appliesTo").element("path").attributeValue("namedElement");
				String partitionPath = p1.attributeValue("namedElement");
				Integer pid = (int) GetID.getId();
				AppendID.AppendID(filepath, GetXPath(partitionPath), pid.toString());

				Element implElement = (Element) document.selectSingleNode(GetXPath(taskpath));

				String decalrepath = "//ownedClassifier[@name='" + implElement.attributeValue("name").split("\\.")[0]
						+ "']";
				String taskid = ((Element) document.selectSingleNode(decalrepath)).attributeValue("id");

				String rtosid = GetElementID(filepath, n.getParent().getUniquePath());

				_partition p = new _partition();
				p.setPartitionid(pid);
				// ???????????????rtos
				p.setRtosid(Integer.valueOf(rtosid));
				component comp = new component();
				comp.setComponentid(pid);
				comp.setType("partition");
				comp.setModeltype("aadl");
				comp.setName(((Element) document.selectSingleNode(GetXPath(partitionPath))).attributeValue("name"));
				camArchMapper.insert(comp);
				ptm.insert(p);
				_task t = new _task();
				t.setTaskid(Integer.valueOf(taskid));
				t.setPartitionid(pid);
				_tm.updateByPrimaryKeySelective(t);
			}
		}
	}

	private static String GetSysname(String systemSubcomponentType) {
		if (systemSubcomponentType != null) {
			String reg = "(?<=#).*\\.[A-Za-z0-9]+";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(systemSubcomponentType);
			ArrayList<String> result = new ArrayList<String>();
			while (matcher.find()) {
				result.add(matcher.group());
			}
			if (result.size() > 0) {
				String[] strings = result.get(0).split("\\.");
				return strings[strings.length - 2] + "." + strings[strings.length - 1];
			}
		}
		return "";
	}

//??????????????????????????????task??????schedule
	private void scheduleResolver(String modelfilename) throws Exception {
		Document document = ModelResolver(modelfilename);
		String getbindings = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedPropertyAssociation";
		List<? extends Node> nodes = document.selectNodes(getbindings);
		for (Node n : nodes) {
			// ??????deploy??????
			Element e = (Element) n;
			// namedElement???????????????impl?????????????????????
			if (e.attributeValue("property").contains("Actual_Processor_Binding")) {

				String taskid = GetSubCompID(modelfilename,
						e.element("appliesTo").element("path").attributeValue("namedElement"));

				Element sb = (Element) document.selectSingleNode(GetXPath(e.element("ownedValue").element("ownedValue")
						.element("ownedListElement").element("path").attributeValue("namedElement")));
				try {
					// memory??????????????????????????????????????????schedule
					Element sbimplElement = (Element) document
							.selectSingleNode(GetXPath42layer(sb.attributeValue("processorSubcomponentType")));
					Element vt = (Element) document
							.selectSingleNode(GetXPath42layer(sbimplElement.element("ownedVirtualProcessorSubcomponent")
									.attributeValue("virtualProcessorSubcomponentType")));

					String schedulename = vt.attributeValue("name");
					Integer i = (int) GetID.getId();
					linkpoint l = new linkpoint();
					l.setLinkpointid(i);
					l.setName(schedulename);
					l.setModeltype("aadl");
					portsMapper.insert(l);
					taskschedule t = new taskschedule();
					t.setTaskscheduleid(i);
					tscmapper.insert(t);

					_task t2 = new _task();
					t2.setTaskid(Integer.valueOf(taskid));
					t2.setTaskscheduleid(i);
					_tm.updateByPrimaryKeySelective(t2);
				} catch (Exception e2) {

					System.out.print(sb.attributeValue("processorSubcomponentType"));
				}
			}

			// linkpoint???????????????,task??????shcedule???id????????????task????????????id(???????????????task?????????????????????)????????????????????????????????????
		}
	}

	public void SetSysFileID(String archfilepath, String sysfilepath) throws Exception {
		Document document = ModelResolver(archfilepath);
		String getsys = "//ownedPublicSection/ownedClassifier[@xsi:type='aadl2:SystemImplementation']/ownedSystemSubcomponent";
		List<? extends Node> nodes = document.selectNodes(getsys);

		for (Node n : nodes) {
			String raw = GetSysname(((Element) n).attributeValue("systemSubcomponentType"));
			String sysname = raw;

			String sys = "//ownedClassifier[@xsi:type='aadl2:SystemImplementation' and @name='" + sysname + "']";
			if (!sysfilepath.contains(";")) {

				document = ModelResolver(sysfilepath);
				Element e = (Element) n;

				AppendID.AppendID(sysfilepath, sys, e.attributeValue("id"));
			} else {
				String[] sysfiles = sysfilepath.split(";");
				for (String s : sysfiles) {
					document = ModelResolver(s);
					Element e = (Element) n;

					AppendID.AppendID(s, sys, e.attributeValue("id"));
				}
			}
		}
	}

	// ??????source??????,??????context??????????????????
	private static String GetName(String modelfilename, String rawpath) throws Exception {
		Document document = ModelResolver(modelfilename);
		CharSequence s = ".";
		if (rawpath.contains(s)) {

			rawpath = GetXPath(rawpath);
		}
		Node node = document.selectSingleNode(rawpath);
		Element e = (Element) node;
		if (e != null)
			return e.attributeValue("name");
		else
			return "";
	}

	private static String GetElementID(String modelfilename, String rawpath) throws Exception {
		Document document = ModelResolver(modelfilename);
		CharSequence s = ".";
		if (rawpath.contains(s)) {

			rawpath = GetXPath(rawpath);
		}
		Node node = document.selectSingleNode(rawpath);
		Element e = (Element) node;
		if (e != null)
			return e.attributeValue("id");
		else
			return "";
	}

//sub????????????impl??????????????????name??????id
	private static String GetSubCompID(String modelfilename, String rawpath) throws Exception {
		Document document = ModelResolver(modelfilename);
		CharSequence s = ".";
		if (rawpath.contains(s)) {
			// sub?????????
			rawpath = GetXPath(rawpath);
		}
		Node node = document.selectSingleNode(rawpath);
		Element e = (Element) node;
		if (e != null) {
			String reg = "(?<=@).[A-Za-z0-9\\.]+";
			ArrayList<String> result = new ArrayList<String>();
			Pattern pattern = Pattern.compile(reg);
			String s1 = "";
			if (e.attributeValue("processorSubcomponentType") != null) {
				s1 = e.attributeValue("processorSubcomponentType");
			} else if (e.attributeValue("processSubcomponentType") != null) {
				s1 = e.attributeValue("processSubcomponentType");
			} else {
				s1 = e.attributeValue("memorySubcomponentType");
			}
			Matcher matcher = pattern.matcher(s1);
			while (matcher.find()) {
				result.add(matcher.group());
			}
			for (int j = 1; j < result.size(); j++) {
				result.set(j, getinc(result.get(j)));
			}
			// IMPL?????????id???
			String name = ((Element) document.selectSingleNode("//" + result.get(0) + "/" + result.get(1)))
					.attributeValue("name").split("\\.")[0];
			return ((Element) document.selectSingleNode("//ownedClassifier[@name='" + name + "']"))
					.attributeValue("id");
		} else
			return "";
	}

	/*
	 * ??????????????????ID,????????????????????????xpath
	 */
	private static String GetXPath(String path) {
		String reg = "(?<=@).[A-Za-z0-9\\.]+";
		ArrayList<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(path);
		while (matcher.find()) {
			result.add(matcher.group());
		}
		for (int j = 1; j < result.size(); j++) {

			result.set(j, getinc(result.get(j)));
		}
		return "//" + result.get(0) + "/" + result.get(1) + "/" + result.get(2);
	}

	private static String GetXPath42layer(String path) {
		String reg = "(?<=@).[A-Za-z0-9\\.]+";
		ArrayList<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(path);
		while (matcher.find()) {
			result.add(matcher.group());
		}
		for (int j = 1; j < result.size(); j++) {

			result.set(j, getinc(result.get(j)));
		}
		return "//" + result.get(0) + "/" + result.get(1);
	}

	// 5???
	private static String GetXPath4State(String path) {
		String reg = "(?<=@).[A-Za-z0-9\\.]+";
		ArrayList<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(path);
		while (matcher.find()) {
			result.add(matcher.group());
		}
		for (int j = 1; j < result.size(); j++) {

			result.set(j, getinc(result.get(j)));
		}
		return "//" + result.get(0) + "/" + result.get(1) + "/" + result.get(2) + "/" + result.get(3) + "/"
				+ result.get(4);
	}

	private static String getinc(String source) {
		CharSequence cs = ".";
		if (source.contains(cs)) {
			String reg4c = "[A-Za-z]+";
			String reg4num = "[0-9]+";

			Pattern pattern = Pattern.compile(reg4c);
			Matcher matcher = pattern.matcher(source);
			List<String> list = new ArrayList<>();
			while (matcher.find()) {
				list.add(matcher.group());
			}
			String c = list.get(0);

			pattern = Pattern.compile(reg4num);
			matcher = pattern.matcher(source);
			list = new ArrayList<>();
			while (matcher.find()) {
				list.add(matcher.group());
			}
			Integer i = Integer.valueOf(list.get(0));
			String num = (++i).toString();

			return c + "[" + num + "]";
		} else {
			return source;
		}

	}
}
