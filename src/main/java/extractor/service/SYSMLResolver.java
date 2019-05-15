package extractor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import extractor.DAO.mapper.InvocationChannelMapper;
import extractor.DAO.mapper.MessageChannelMapper;
import extractor.DAO.mapper._eventMapper;
import extractor.DAO.mapper._exceptionMapper;
import extractor.DAO.mapper._provideMapper;
import extractor.DAO.mapper._requireMapper;
import extractor.DAO.mapper._stateMapper;
import extractor.DAO.mapper._taskMapper;
import extractor.DAO.mapper.busMapper;
import extractor.DAO.mapper.communicationchannelMapper;
import extractor.DAO.mapper.componentMapper;
import extractor.DAO.mapper.deviceMapper;
import extractor.DAO.mapper.linkpointMapper;
import extractor.DAO.mapper.rtosMapper;
import extractor.DAO.mapper.transitionMapper;
import extractor.DAO.mapper.transitionstateMapper;
import extractor.model.InvocationChannel;
import extractor.model.MessageChannel;
import extractor.model._event;
import extractor.model._exception;
import extractor.model._provide;
import extractor.model._require;
import extractor.model._state;
import extractor.model._task;
import extractor.model.bus;
import extractor.model.communicationchannel;
import extractor.model.component;
import extractor.model.device;
import extractor.model.linkpoint;
import extractor.model.rtos;
import extractor.model.transition;
import extractor.model.transitionstate;

@Service("SYSMLResolver")
public class SYSMLResolver {
	Map<String, String> sysmlFiles = new HashMap<String, String>();
	static String modeldirectory;
	static List<component> componentlist = new ArrayList<component>();
	static List<linkpoint> portlist = new ArrayList<linkpoint>();
	static List<_require> requirelist = new ArrayList<_require>();
	static List<_provide> providelist = new ArrayList<_provide>();

	static List<_exception> exceptionlist = new ArrayList<_exception>();
	static List<communicationchannel> cclist = new ArrayList<communicationchannel>();
	static List<_task> tasklist = new ArrayList<_task>();
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
		Integer aIntegers = camArchMapper.getPortIDByComponentName(name, portname);
		return camArchMapper.getPortIDByComponentName(name, portname);
	}

	public Integer getCmpIDbyName(String name) {
		return camArchMapper.getIDbyName(name).getComponentid();
	}

	public Integer getCChannelBysd(Integer sid, Integer did) {
		return cchannelMapper.getgetCChannelBysd(sid, did);
	}

	public Integer getEventID(String eventname) {
		return em.getEventID(eventname).getEventid();
	}

	public Integer getStateID(String statename) {
		return sm.getStateID(statename);
	}

	public void setSysmlFiles(Map<String, String> sysmlFiles) {
		this.sysmlFiles = sysmlFiles;
	}

	public void srvmatchmeta() throws Exception {
		modeldirectory = "src/main/resources/modelresource/MarkedModelFile/";
		MatchComponents(sysmlFiles.get("文件"), "块图");
		componentlist.forEach((v) -> {
			insert_component(v);
		});
		portlist.forEach((v) -> {
			insert_ports(v);
		});
		tasklist.forEach((v) -> {
			insert_task(v);
		});
		exceptionlist.forEach((v) -> {
			insert_exception(v);
		});
		MatchCChannel(sysmlFiles.get("文件"), "块图");
		cclist.forEach((v) -> {
			insert_cchannel(v);
		});
	}

	public static Document ModelResolver(String url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	public void MatchComponents(String filepath, String contenttype) throws Exception {
		Document document = ModelResolver(filepath);

		String getCompponents = "//packagedElement[@xmi:type='uml:Class']";
		List<? extends Node> com = document.selectNodes(getCompponents);
		for (Node n : com) {
			Element e = (Element) n;
			component c = new component();
			// 自带的xmi:id
			Integer idString = (int) GetID.getId();
			AppendID.AppendID(filepath, n.getUniquePath(), idString.toString());
			c.setComponentid(idString);
			c.setName(e.attributeValue("name"));
			c.setType("sysml");
			componentlist.add(c);
			if (e.element("ownedRule[@xmi:type='uml:Constraint']") != null) {
				Element e2 = e.element("ownedRule[@xmi:type='uml:Constraint']");
				_exception ex = new _exception();
				ex.setName(e2.attributeValue("name"));
				// ex.setCommunicationchannelid(communicationchannelid);
				exceptionlist.add(ex);
			}
			LinkpointResolver(filepath, n.getUniquePath(), c);
			TaskResolver(filepath, n.getUniquePath(), c);
		}
	}

	private static void LinkpointResolver(String linkpointfile, String fatherpath, component father) throws Exception {
		Document document = ModelResolver(linkpointfile);
		List<? extends Node> ports = document.selectNodes(fatherpath + "/ownedAttribute[@xmi:type='uml:Port']");
		for (Node n : ports) {
			Element element2 = (Element) n;
			linkpoint ports1 = new linkpoint();
			ports1.setName(element2.attributeValue("name"));
			Integer linkpointID = (int) GetID.getId();
			ports1.setLinkpointid(linkpointID);
			try {
				AppendID.AppendID(linkpointfile, element2.getUniquePath(), linkpointID.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			portlist.add(ports1);

		}
	}

	public void MatchCChannel(String modelfilename, String modelType) throws Exception {
		Document document = ModelResolver(modelfilename);
		String getmessagechannel = "//packagedElement[@xmi:type='uml:InformationFlow']";
		List<? extends Node> namelist = document.selectNodes(getmessagechannel);
		for (Node n : namelist) {
			Element element = (Element) n;
			communicationchannel cchannel = new communicationchannel();
			Integer idString = (int) GetID.getId();
			AppendID.AppendID(modelfilename, n.getUniquePath(), idString.toString());
			cchannel.setName(element.attributeValue("name"));
			cchannel.setCommunicationchannelid(idString);
			cchannel.setSourceid(GetCMPIDByXMIID(modelfilename, element.attributeValue("informationSource")));
			cchannel.setDestid(GetCMPIDByXMIID(modelfilename, element.attributeValue("informationTarget")));
			cclist.add(cchannel);
		}
	}

	private static void TaskResolver(String modelfilename, String fatherpath, component father) throws Exception {
		Document document = ModelResolver(modelfilename);
		String gettask = fatherpath + "/ownedOperation[@xmi:type='uml:Operation']";
		List<? extends Node> namelist = document.selectNodes(gettask);
		for (Node n : namelist) {
			Element element2 = (Element) n;
			component component = new component();
			Integer idString = (int) GetID.getId();
			component.setComponentid(idString);

			component.setModeltype("sysml");
			component.setName(element2.attributeValue("name"));
			component.setType("sysmltask");
			componentlist.add(component);
			_task t = new _task();
			t.setName(element2.attributeValue("name"));
			t.setTaskid(idString);
			//TODO 块图的partition不存在
			//t.setPartitionid(father.getComponentid());
			try {
				AppendID.AppendID(modelfilename, element2.getUniquePath(), t.getTaskid().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			tasklist.add(t);
			// operation即task有错误定义
			if (element2.element("ownedRule") != null) {
				// TODO 解析错误
				// Element e2 = element2.element("ownedRule[@xmi:type='uml:Constraint']");
				Element e2 = element2.element("ownedRule");
				_exception ex = new _exception();
				ex.setName(e2.attributeValue("name"));
				exceptionlist.add(ex);
			}
		}

	}

	private Integer GetCMPIDByXMIID(String filepath, String id) throws Exception {
		Document document = ModelResolver(filepath);
		String g = "//packagedElement[@xmi:type='uml:Class' and xmi:id='" + id + "']";
		Element element = (Element) document.selectSingleNode(g);
		return Integer.valueOf(element.attributeValue("id"));
	}
}