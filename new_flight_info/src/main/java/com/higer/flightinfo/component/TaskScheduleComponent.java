package com.higer.flightinfo.component;

import com.higer.flightinfo.entity.TFlightChange;
import com.higer.flightinfo.entity.TFlightData;
import com.higer.flightinfo.entity.TFlightProcessNode;
import com.higer.flightinfo.repository.TFlightChangeRepository;
import com.higer.flightinfo.repository.TFlightDataRepository;
import com.higer.flightinfo.repository.TFlightProcessNodeRepository;
import com.higer.flightinfo.util.XmlTransformUtil;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.stream.Collectors;


@EnableAsync
@Component
public class TaskScheduleComponent {

    @Autowired
    private TFlightChangeRepository repository;

    @Autowired
    private TFlightDataRepository tFlightDataRepository;

    @Autowired
    private TFlightProcessNodeRepository tFlightProcessNodeRepository;

    @Async
    public ListenableFuture<TFlightChange> saveTFlightChangeLog(Element body) throws InstantiationException, IllegalAccessException {
        TFlightChange change = XmlTransformUtil.transformXml(body,TFlightChange.class);
        AsyncResult<TFlightChange> tFlightChangeAsyncResult = new AsyncResult<>(repository.save(change));
        tFlightChangeAsyncResult.addCallback(new TFlightFutureCallback<>(change));
        return tFlightChangeAsyncResult;

    }

    @Async
    public ListenableFuture<TFlightData> saveTFlightData(Element body) throws InstantiationException, IllegalAccessException {
        TFlightData data = XmlTransformUtil.transformXml(body,TFlightData.class);
        Element processNode = body.element("ProcessNode");
        List<Element> items = processNode.elements("item");
        List<TFlightProcessNode> collect = items.stream().map(item -> {
            TFlightProcessNode node=new TFlightProcessNode();
            node.setFid(data.getFid());
            node.setItem(item.attributeValue("id"));
            node.setProcessNodeName(item.elementTextTrim("ProcessNodeName"));
            node.setAirport(item.elementTextTrim("Airport"));
            Element processNodeTime = item.element("ProcessNodeTime");
            processNodeTime.elements("item").stream().map(_item->_item.elementTextTrim("ProcessNodeTime")).sorted().findFirst().ifPresent(node::setProcessNodeTime);
            return node;
        }).peek(item->item.setFid(data.getFid())).collect(Collectors.toList());
//        tFlightProcessNodeRepository.saveAll(collect);
        data.setProcessNode(collect);
        AsyncResult<TFlightData> tFlightDataAsyncResult = new AsyncResult<>(tFlightDataRepository.save(data));
        tFlightDataAsyncResult.addCallback(new TFlightFutureCallback<>(data));
        return tFlightDataAsyncResult;
    }
}
