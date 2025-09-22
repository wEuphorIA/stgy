package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.attr.AttrValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.atguigu.lease.web.admin.vo.room.RoomDetailVo;
import com.atguigu.lease.web.admin.vo.room.RoomItemVo;
import com.atguigu.lease.web.admin.vo.room.RoomQueryVo;
import com.atguigu.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 @author liubo
 @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 @createDate 2023-07-24 15:48:00 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private AttrValueMapper attrValueMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private RoomInfoMapper RoomInfoMapper;

    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private RoomAttrValueService roomAttrValueService;

    @Autowired
    private RoomFacilityService roomFacilityService;

    @Autowired
    private RoomLabelService roomLabelService;

    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;

    @Autowired
    private RoomLeaseTermService roomLeaseTermService;

    @Autowired
    private RoomInfoMapper roomInfoMapper;


    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
        boolean isUpdate = roomSubmitVo.getId() != null;
        super.saveOrUpdate(roomSubmitVo);
        //更新操作
        if (isUpdate) {

            //1.删除原有graphInfoList
            LambdaUpdateWrapper<GraphInfo> graphInfoWrapper = new LambdaUpdateWrapper<>();
            graphInfoWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphInfoWrapper.eq(GraphInfo::getItemId, roomSubmitVo.getId());
            graphInfoService.remove(graphInfoWrapper);

            //2.删除原有roomAttrValueList
            LambdaQueryWrapper<RoomAttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
            attrValueWrapper.eq(RoomAttrValue::getRoomId, roomSubmitVo.getId());
            roomAttrValueService.remove(attrValueWrapper);

            //3.删除原有roomFacilityList
            LambdaQueryWrapper<RoomFacility> facilityWrapper = new LambdaQueryWrapper<>();
            facilityWrapper.eq(RoomFacility::getRoomId, roomSubmitVo.getId());
            roomFacilityService.remove(facilityWrapper);

            //4.删除原有roomLabelList
            LambdaQueryWrapper<RoomLabel> labelWrapper = new LambdaQueryWrapper<>();
            labelWrapper.eq(RoomLabel::getRoomId, roomSubmitVo.getId());
            roomLabelService.remove(labelWrapper);

            //5.删除原有paymentTypeList
            LambdaQueryWrapper<RoomPaymentType> paymentTypeWrapper = new LambdaQueryWrapper<>();
            paymentTypeWrapper.eq(RoomPaymentType::getRoomId, roomSubmitVo.getId());
            roomPaymentTypeService.remove(paymentTypeWrapper);

            //6.删除原有leaseTermList
            LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermWrapper = new LambdaQueryWrapper<>();
            roomLeaseTermWrapper.eq(RoomLeaseTerm::getRoomId, roomSubmitVo.getId());
            roomLeaseTermService.remove(roomLeaseTermWrapper);
        }

        //1.保存新的graphInfoList
        List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();

        if (!CollectionUtils.isEmpty(graphVoList)) {
            ArrayList<GraphInfo> graphInfos = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.ROOM);
                graphInfo.setItemId(roomSubmitVo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfos.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfos);
        }

        //2.保存新的roomAttrValueList
        List<Long> attrValueIds = roomSubmitVo.getAttrValueIds();
        if (!CollectionUtils.isEmpty(attrValueIds)) {
            ArrayList<RoomAttrValue> roomAttrValues = new ArrayList<>();
            for (Long attrValueId : attrValueIds) {
                RoomAttrValue roomAttrValue = RoomAttrValue.builder()
                        .roomId(roomSubmitVo.getId()).attrValueId(attrValueId).build();
                roomAttrValues.add(roomAttrValue);
            }
            roomAttrValueService.saveBatch(roomAttrValues);
        }

        //3.保存新的facilityInfoList
        List<Long> facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)) {
            ArrayList<RoomFacility> roomFacilities = new ArrayList<>();
            for (Long facilityInfoId : facilityInfoIds) {
                RoomFacility roomFacility = RoomFacility.builder()
                        .roomId(roomSubmitVo.getId()).facilityId(facilityInfoId).build();
                roomFacilities.add(roomFacility);
            }
            roomFacilityService.saveBatch(roomFacilities);

        }

        //4.保存新的labelInfoList
        List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (!CollectionUtils.isEmpty(labelInfoIds)) {
            ArrayList<RoomLabel> roomLabels = new ArrayList<>();
            for (Long labelInfoId : labelInfoIds) {
                RoomLabel roomLabel = RoomLabel.builder()
                        .roomId(roomSubmitVo.getId()).labelId(labelInfoId).build();
                roomLabels.add(roomLabel);
            }
            roomLabelService.saveBatch(roomLabels);
        }

        //5.保存新的paymentTypeList
        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if (!CollectionUtils.isEmpty(paymentTypeIds)) {
            ArrayList<RoomPaymentType> roomPaymentTypes = new ArrayList<>();
            for (Long paymentTypeId : paymentTypeIds) {
                RoomPaymentType roomPaymentType = RoomPaymentType.builder()
                        .roomId(roomSubmitVo.getId()).paymentTypeId(paymentTypeId).build();
                roomPaymentTypes.add(roomPaymentType);
            }
            roomPaymentTypeService.saveBatch(roomPaymentTypes);
        }

        //6.保存新的leaseTermList
        List<Long> leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (!CollectionUtils.isEmpty(leaseTermIds)) {
            ArrayList<RoomLeaseTerm> roomLeaseTerms = new ArrayList<>();
            for (Long leaseTermId : leaseTermIds) {
                RoomLeaseTerm roomLeaseTerm = RoomLeaseTerm.builder()
                        .roomId(roomSubmitVo.getId()).leaseTermId(leaseTermId).build();
                roomLeaseTerms.add(roomLeaseTerm);
            }
            roomLeaseTermService.saveBatch(roomLeaseTerms);
        }
    }

    @Override
    public IPage<RoomItemVo> pageRoomItemByQuery(IPage<RoomItemVo> page, RoomQueryVo queryVo) {
        return roomInfoMapper.pageRoomItemByQuery(page,queryVo);
    }

    @Override
    public RoomDetailVo getRoomDetailById(Long id) {

        //1.查询RoomInfo
        RoomInfo roomInfo = RoomInfoMapper.selectById(id);

        //2.查询所属公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(roomInfo.getApartmentId());

        //3.查询graphInfoList
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);

        //4.查询attrValueList
        List<AttrValueVo> attrvalueVoList = attrValueMapper.selectListByRoomId(id);

        //5.查询facilityInfoList
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);

        //6.查询labelInfoList
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByRoomId(id);

        //7.查询paymentTypeList
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectListByRoomId(id);

        //8.查询leaseTermList
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectListByRoomId(id);


        //组装数据
        RoomDetailVo roomDetailVo = new RoomDetailVo();

        BeanUtils.copyProperties(roomInfo,roomDetailVo);

        roomDetailVo.setApartmentInfo(apartmentInfo);

        roomDetailVo.setGraphVoList(graphVoList);

        roomDetailVo.setAttrValueVoList(attrvalueVoList);

        roomDetailVo.setFacilityInfoList(facilityInfoList);

        roomDetailVo.setLabelInfoList(labelInfoList);

        roomDetailVo.setPaymentTypeList(paymentTypeList);

        roomDetailVo.setLeaseTermList(leaseTermList);
        return roomDetailVo;
    }

    @Override
    public void removeRoomById(Long id) {

        //1.删除RoomInfo
        super.removeById(id);

        //2.删除原有graphInfoList
        LambdaUpdateWrapper<GraphInfo> graphInfoWrapper = new LambdaUpdateWrapper<>();
        graphInfoWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
        graphInfoWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphInfoWrapper);

        //3.删除原有roomAttrValueList
        LambdaQueryWrapper<RoomAttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
        attrValueWrapper.eq(RoomAttrValue::getRoomId, id);
        roomAttrValueService.remove(attrValueWrapper);

        //4.删除原有roomFacilityList
        LambdaQueryWrapper<RoomFacility> facilityWrapper = new LambdaQueryWrapper<>();
        facilityWrapper.eq(RoomFacility::getRoomId, id);
        roomFacilityService.remove(facilityWrapper);

        //5.删除原有roomLabelList
        LambdaQueryWrapper<RoomLabel> labelWrapper = new LambdaQueryWrapper<>();
        labelWrapper.eq(RoomLabel::getRoomId, id);
        roomLabelService.remove(labelWrapper);

        //6.删除原有paymentTypeList
        LambdaQueryWrapper<RoomPaymentType> paymentTypeWrapper = new LambdaQueryWrapper<>();
        paymentTypeWrapper.eq(RoomPaymentType::getRoomId, id);
        roomPaymentTypeService.remove(paymentTypeWrapper);

        //7.删除原有leaseTermList
        LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermWrapper = new LambdaQueryWrapper<>();
        roomLeaseTermWrapper.eq(RoomLeaseTerm::getRoomId, id);
        roomLeaseTermService.remove(roomLeaseTermWrapper);

    }
}




