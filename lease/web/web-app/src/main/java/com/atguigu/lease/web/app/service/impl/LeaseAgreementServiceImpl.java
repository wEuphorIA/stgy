package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.app.mapper.*;
import com.atguigu.lease.web.app.service.LeaseAgreementService;
import com.atguigu.lease.web.app.vo.agreement.AgreementDetailVo;
import com.atguigu.lease.web.app.vo.agreement.AgreementItemVo;
import com.atguigu.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {


    @Autowired
    private LeaseAgreementMapper agreementMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;


    @Override
    public List<AgreementItemVo> listItemByPhone(String phone) {
        return agreementMapper.listItemByPhone(phone);
    }

    @Override
    public AgreementDetailVo getDetailById(Long id) {

        LeaseAgreement leaseAgreement = agreementMapper.selectById(id);

        if (leaseAgreement == null) {
            return null;
        }

        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());

        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());

        List<GraphVo> roomGraphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, leaseAgreement.getRoomId());

        List<GraphVo> apartmentGraphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, leaseAgreement.getRoomId());

        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());

        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());

        AgreementDetailVo agreementDetailVo = new AgreementDetailVo();

        BeanUtils.copyProperties(leaseAgreement,agreementDetailVo);

        agreementDetailVo.setApartmentName(apartmentInfo.getName());

        agreementDetailVo.setApartmentGraphVoList(apartmentGraphVoList);

        agreementDetailVo.setRoomId(roomInfo.getId());

        agreementDetailVo.setRoomGraphVoList(roomGraphVoList);

        agreementDetailVo.setPaymentTypeName(paymentType.getName());

        agreementDetailVo.setLeaseTermUnit(leaseTerm.getUnit());

        agreementDetailVo.setLeaseTermMonthCount(leaseTerm.getMonthCount());

        return agreementDetailVo;
    }
}




