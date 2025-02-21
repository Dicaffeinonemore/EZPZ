import React from 'react';
import './ParkingFeeModal.css';

const RSUFeeModal = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>여수공항 주차요금 안내</h2>
          <button className="close-button" onClick={onClose}>×</button>
        </div>
        <div className="modal-body">
          <table className="fee-table">
            <thead>
              <tr>
                <th className="center">구분</th>
                <th className="center">소형</th>
                <th className="center">대형</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td className="center">주차요금: 최초10분</td>
                <td className="center">무료</td>
                <td className="center">무료</td>
              </tr>
              <tr>
                <td className="center">주차요금: 기본30분</td>
                <td className="center">500원</td>
                <td className="center">800원</td>
              </tr>
              <tr>
                <td className="center">매 10분마다 추가요금</td>
                <td className="center">200원</td>
                <td className="center">300원</td>
              </tr>
              <tr>
                <td className="center">1일 주차요금: 24시간</td>
                <td className="center">5,000원</td>
                <td className="center">10,000원</td>
              </tr>
              <tr>
                <td className="center">추가 1일마다</td>
                <td className="center">5,000원</td>
                <td className="center">10,000원</td>
              </tr>
            </tbody>
          </table>

          <div className="parking-notice">
            <p className="special-notice">• 최초 10분 무료 주차 가능</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RSUFeeModal;